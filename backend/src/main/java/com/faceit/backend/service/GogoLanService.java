package com.faceit.backend.service;

import com.faceit.backend.config.GogoLanProperties;
import com.faceit.backend.dto.MapStatsDTO;
import com.faceit.backend.dto.event.EventAwardDTO;
import com.faceit.backend.dto.event.EventAwardWinnerDTO;
import com.faceit.backend.dto.event.EventDayStatsDTO;
import com.faceit.backend.dto.event.EventMapSummaryDTO;
import com.faceit.backend.dto.event.EventPlayerStatsDTO;
import com.faceit.backend.dto.event.EventQueueComboDTO;
import com.faceit.backend.dto.event.GogoLanEventDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;
import com.faceit.backend.model.FaceitV4MatchStatsResponse;
import com.faceit.backend.service.util.FaceitLifetimeAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GogoLanService {
    private static final long CACHE_DURATION_MILLIS = 5 * 60 * 1000;
    private static final long MATCH_STATS_CACHE_DURATION_MILLIS = 60 * 60 * 1000;

    private final WebClient unauthenticatedWebClient;
    private final WebClient authenticatedWebClient;
    private final GogoLanProperties properties;
    private final Map<String, CacheEntry> eventCache = new ConcurrentHashMap<>();
    private final Map<String, MatchStatsCacheEntry> v4MatchStatsCache = new ConcurrentHashMap<>();

    public GogoLanService(@Qualifier("unauthenticatedWebClient") WebClient unauthenticatedWebClient,
                          @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient,
                          GogoLanProperties properties) {
        this.unauthenticatedWebClient = unauthenticatedWebClient;
        this.authenticatedWebClient = authenticatedWebClient;
        this.properties = properties;
    }

    public GogoLanEventDTO getEvent() {
        return getEvent(null, null);
    }

    public GogoLanEventDTO getEvent(LocalDate startDateOverride, LocalDate endDateOverride) {
        LocalDate startDate = startDateOverride != null ? startDateOverride : properties.getStartDate();
        LocalDate endDate = endDateOverride != null ? endDateOverride : properties.getEndDate();
        String cacheKey = startDate + "|" + endDate;
        long now = System.currentTimeMillis();
        CacheEntry cached = eventCache.get(cacheKey);
        if (cached != null && now < cached.expiresAtMillis()) {
            return cached.dto();
        }
        ZoneId zoneId = ZoneId.of(properties.getZoneId());
        long startMillis = startDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
        long endMillis = endDate.plusDays(1).atStartOfDay(zoneId).minusNanos(1).toInstant().toEpochMilli();

        Map<String, FaceitPlayerInfo> profiles = new ConcurrentHashMap<>();
        Map<String, List<MatchStats>> eventMatchesByPlayer = new ConcurrentHashMap<>();

        properties.getPlayerIds().parallelStream().forEach(playerId -> {
            profiles.put(playerId, fetchPlayerInfo(playerId));
            eventMatchesByPlayer.put(playerId, fetchWindowedMatches(playerId, startMillis, endMillis));
        });

        List<MatchStats> allMatches = eventMatchesByPlayer.values().stream()
                .flatMap(List::stream)
                .toList();

        List<EventPlayerStatsDTO> players = properties.getPlayerIds().stream()
                .map(playerId -> toPlayerStats(
                        playerId,
                        profiles.get(playerId),
                        eventMatchesByPlayer.getOrDefault(playerId, List.of()),
                        zoneId,
                        startDate,
                        endDate))
                .sorted(Comparator.comparingInt(EventPlayerStatsDTO::getTotalEloGain).reversed()
                        .thenComparing(Comparator.comparingDouble(EventPlayerStatsDTO::getAvgKd).reversed()))
                .toList();
        Map<String, AdvancedStatsAccumulator> advancedStatsByPlayer = buildAdvancedStats(eventMatchesByPlayer);
        players.forEach(player -> applyAdvancedStats(player, advancedStatsByPlayer.get(player.getPlayerId())));

        GogoLanEventDTO dto = new GogoLanEventDTO();
        dto.setKey(properties.getKey());
        dto.setName(properties.getName());
        dto.setZoneId(properties.getZoneId());
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setPlayerCount(players.size());
        dto.setTotalUniqueMatches((int) allMatches.stream()
                .map(MatchStats::getMatchId)
                .filter(Objects::nonNull)
                .distinct()
                .count());
        dto.setPlayers(players);
        dto.setAwards(buildAwards(players));
        dto.setQueueCombos(buildQueueCombos(players, eventMatchesByPlayer));
        dto.setMapLeaderboard(buildEventMapLeaderboard(allMatches));
        eventCache.put(cacheKey, new CacheEntry(dto, now + CACHE_DURATION_MILLIS));
        return dto;
    }

    private FaceitPlayerInfo fetchPlayerInfo(String playerId) {
        return authenticatedWebClient.get()
                .uri("/data/v4/players/{playerId}", playerId)
                .retrieve()
                .bodyToMono(FaceitPlayerInfo.class)
                .block();
    }

    private FaceitV4MatchStatsResponse fetchV4MatchStats(String matchId) {
        long now = System.currentTimeMillis();
        MatchStatsCacheEntry cached = v4MatchStatsCache.get(matchId);
        if (cached != null && now < cached.expiresAtMillis()) {
            return cached.response();
        }

        FaceitV4MatchStatsResponse response = authenticatedWebClient.get()
                .uri("/data/v4/matches/{matchId}/stats", matchId)
                .retrieve()
                .bodyToMono(FaceitV4MatchStatsResponse.class)
                .block();

        if (response != null) {
            v4MatchStatsCache.put(matchId, new MatchStatsCacheEntry(response, now + MATCH_STATS_CACHE_DURATION_MILLIS));
        }
        return response;
    }
    private List<MatchStats> fetchWindowedMatches(String playerId, long startMillis, long endMillis) {
        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";
        List<MatchStats> filteredMatches = new ArrayList<>();
        MatchStats baselineMatch = null;
        long to = endMillis;

        while (true) {
            List<MatchStats> page = unauthenticatedWebClient.get()
                    .uri(baseUrl + "&to=" + to)
                    .retrieve()
                    .bodyToFlux(MatchStats.class)
                    .collectList()
                    .block();

            if (page == null || page.isEmpty()) {
                break;
            }

            for (MatchStats match : page) {
                long date = match.getDate();
                if (date >= startMillis && date <= endMillis) {
                    filteredMatches.add(match);
                } else if (date < startMillis && baselineMatch == null) {
                    baselineMatch = match;
                }
            }

            long oldestDate = page.get(page.size() - 1).getDate();
            if (oldestDate < startMillis || page.size() < 100) {
                break;
            }
            to = oldestDate - 1;
        }

        filteredMatches.sort(Comparator.comparing(MatchStats::getDate).reversed());

        if (filteredMatches.isEmpty()) {
            return filteredMatches;
        }

        List<MatchStats> workingMatches = new ArrayList<>(filteredMatches);
        if (baselineMatch != null) {
            workingMatches.add(baselineMatch);
            workingMatches.sort(Comparator.comparing(MatchStats::getDate).reversed());
        }
        FaceitLifetimeAggregator.calculateEloGain(workingMatches);
        return filteredMatches;
    }

    private EventPlayerStatsDTO toPlayerStats(String playerId,
                                              FaceitPlayerInfo profile,
                                              List<MatchStats> matches,
                                              ZoneId zoneId,
                                              LocalDate startDate,
                                              LocalDate endDate) {
        EventPlayerStatsDTO dto = new EventPlayerStatsDTO();
        dto.setPlayerId(playerId);
        dto.setNickname(profile != null ? profile.getNickname() : playerId);
        dto.setAvatar(profile != null ? profile.getAvatar() : null);
        dto.setCountry(profile != null ? profile.getCountry() : "");
        dto.setSkillLevel(profile != null ? profile.getSkillLevel() : 0);
        dto.setFaceitElo(profile != null ? profile.getFaceitElo() : 0);

        if (matches == null || matches.isEmpty()) {
            dto.setDailyStats(buildEmptyDailyStats(startDate, endDate));
            dto.setMapStats(List.of());
            dto.setLast5Results(List.of());
            return dto;
        }

        FaceitLifetimeAggregator.LifetimeStats stats = FaceitLifetimeAggregator.aggregate(matches);
        dto.setMatchesPlayed(stats.matchesPlayed());
        dto.setWins(stats.totalWins());
        dto.setLosses(stats.totalLosses());
        dto.setWinrate(winrate(stats.totalWins(), stats.matchesPlayed()));
        dto.setTotalEloGain(stats.totalEloGain());
        dto.setEloChange(stats.eloChange());
        dto.setAvgKd(stats.avgKd());
        dto.setAvgKr(stats.avgKr());
        dto.setAvgAdr(stats.avgAdr());
        dto.setAvgHsPercent(stats.avgHsPercent());
        dto.setKavg(stats.kavg());
        dto.setAavg(stats.aavg());
        dto.setDavg(stats.davg());
        dto.setLongestWinStreak(stats.longestWinStreak());
        dto.setLongestLossStreak(stats.longestLossStreak());
        dto.setLast5Results(stats.last5Results());
        dto.setLastMatchAt(Instant.ofEpochMilli(matches.get(0).getDate()));

        List<MapStatsDTO> mapStats = FaceitLifetimeAggregator.aggregateByMap(matches).stream()
                .sorted(Comparator.comparingInt(MapStatsDTO::getMatches).reversed()
                        .thenComparing(Comparator.comparingInt(MapStatsDTO::getWinrate).reversed()))
                .toList();
        dto.setMapStats(mapStats);
        dto.setMostPlayedMap(mapStats.stream().max(Comparator.comparingInt(MapStatsDTO::getMatches)).map(MapStatsDTO::getMap).orElse(null));
        dto.setBestMap(mapStats.stream()
                .max(Comparator.comparingInt(MapStatsDTO::getWinrate)
                        .thenComparing(MapStatsDTO::getAvgKd)
                        .thenComparingInt(MapStatsDTO::getMatches))
                .map(MapStatsDTO::getMap)
                .orElse(null));
        dto.setWorstMap(mapStats.stream()
                .min(Comparator.comparingInt(MapStatsDTO::getWinrate)
                        .thenComparing(MapStatsDTO::getAvgKd)
                        .thenComparingInt(MapStatsDTO::getMatches))
                .map(MapStatsDTO::getMap)
                .orElse(null));

        List<EventDayStatsDTO> dailyStats = buildDailyStats(matches, zoneId, startDate, endDate);
        dto.setDailyStats(dailyStats);
        dto.setBestDay(dailyStats.stream()
                .filter(day -> day.getMatchesPlayed() > 0)
                .max(Comparator.comparingInt(EventDayStatsDTO::getTotalEloGain)
                        .thenComparing(EventDayStatsDTO::getAvgKd)
                        .thenComparingInt(EventDayStatsDTO::getWinrate))
                .orElse(null));
        dto.setWorstDay(dailyStats.stream()
                .filter(day -> day.getMatchesPlayed() > 0)
                .min(Comparator.comparingInt(EventDayStatsDTO::getTotalEloGain)
                        .thenComparing(EventDayStatsDTO::getAvgKd)
                        .thenComparingInt(EventDayStatsDTO::getWinrate))
                .orElse(null));
        int totalSessionMinutes = dailyStats.stream().mapToInt(EventDayStatsDTO::getSessionMinutes).sum();
        dto.setTotalSessionMinutes(totalSessionMinutes);
        dto.setTotalSessionHours(round(totalSessionMinutes / 60.0));
        return dto;
    }

    private List<EventDayStatsDTO> buildDailyStats(List<MatchStats> matches,
                                                   ZoneId zoneId,
                                                   LocalDate startDate,
                                                   LocalDate endDate) {
        Map<LocalDate, List<MatchStats>> grouped = matches.stream()
                .collect(Collectors.groupingBy(match -> Instant.ofEpochMilli(match.getDate()).atZone(zoneId).toLocalDate()));

        List<EventDayStatsDTO> days = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            days.add(toDayStats(date, grouped.getOrDefault(date, List.of())));
            date = date.plusDays(1);
        }
        return days;
    }

    private List<EventDayStatsDTO> buildEmptyDailyStats(LocalDate startDate, LocalDate endDate) {
        List<EventDayStatsDTO> days = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            days.add(toDayStats(date, List.of()));
            date = date.plusDays(1);
        }
        return days;
    }

    private EventDayStatsDTO toDayStats(LocalDate date, List<MatchStats> matches) {
        EventDayStatsDTO dto = new EventDayStatsDTO();
        dto.setDate(date);

        if (matches == null || matches.isEmpty()) {
            dto.setLastResults(List.of());
            return dto;
        }

        List<MatchStats> sorted = matches.stream()
                .sorted(Comparator.comparing(MatchStats::getDate).reversed())
                .toList();
        FaceitLifetimeAggregator.LifetimeStats stats = FaceitLifetimeAggregator.aggregate(sorted);
        dto.setMatchesPlayed(stats.matchesPlayed());
        dto.setWins(stats.totalWins());
        dto.setLosses(stats.totalLosses());
        dto.setWinrate(winrate(stats.totalWins(), stats.matchesPlayed()));
        dto.setTotalEloGain(stats.totalEloGain());
        dto.setAvgKd(stats.avgKd());
        dto.setAvgKr(stats.avgKr());
        dto.setAvgAdr(stats.avgAdr());
        dto.setAvgHsPercent(stats.avgHsPercent());
        dto.setKavg(stats.kavg());
        dto.setAavg(stats.aavg());
        dto.setDavg(stats.davg());
        dto.setLastResults(stats.last5Results());
        long totalDurationMillis = sorted.stream()
                .mapToLong(this::matchDurationMillis)
                .sum();
        if (totalDurationMillis <= 0) {
            long latest = sorted.get(0).getDate();
            long earliest = sorted.get(sorted.size() - 1).getDate();
            totalDurationMillis = Math.max(0L, latest - earliest);
        }

        int sessionMinutes = (int) Math.round(totalDurationMillis / 60000.0);
        dto.setSessionMinutes(sessionMinutes);
        dto.setSessionHours(round(sessionMinutes / 60.0));
        return dto;
    }
    private long matchDurationMillis(MatchStats match) {
        long startedAt = normalizeEpochMillis(match.getCreatedAt());
        long finishedAt = normalizeEpochMillis(match.getDate());
        if (startedAt > 0 && finishedAt > startedAt) {
            return finishedAt - startedAt;
        }
        return 0L;
    }

    private long normalizeEpochMillis(long value) {
        if (value <= 0) {
            return 0L;
        }
        return value < 100000000000L ? value * 1000L : value;
    }

    private List<EventQueueComboDTO> buildQueueCombos(List<EventPlayerStatsDTO> players,
                                                      Map<String, List<MatchStats>> matchesByPlayer) {
        Map<String, String> nicknameByPlayerId = players.stream()
                .collect(Collectors.toMap(EventPlayerStatsDTO::getPlayerId, EventPlayerStatsDTO::getNickname));
        Map<String, List<PlayerMatchRef>> sharedMatches = new LinkedHashMap<>();

        for (Map.Entry<String, List<MatchStats>> entry : matchesByPlayer.entrySet()) {
            for (MatchStats match : entry.getValue()) {
                String key = match.getMatchId() + "|" + match.getTeamId();
                sharedMatches.computeIfAbsent(key, ignored -> new ArrayList<>())
                        .add(new PlayerMatchRef(entry.getKey(), match));
            }
        }

        Map<String, QueueAccumulator> combos = new HashMap<>();

        for (List<PlayerMatchRef> refs : sharedMatches.values()) {
            if (refs.size() < 2) {
                continue;
            }

            List<PlayerMatchRef> sortedRefs = refs.stream()
                    .sorted(Comparator.comparing(ref -> nicknameByPlayerId.getOrDefault(ref.playerId(), ref.playerId())))
                    .toList();

            for (int size = 2; size <= sortedRefs.size(); size++) {
                buildCombinations(sortedRefs, size, 0, new ArrayList<>(), combos, nicknameByPlayerId);
            }
        }

        return combos.values().stream()
                .map(QueueAccumulator::toDto)
                .sorted(Comparator.comparingInt(EventQueueComboDTO::getLineupSize).reversed()
                        .thenComparing(Comparator.comparingInt(EventQueueComboDTO::getMatchesPlayed).reversed())
                        .thenComparing(Comparator.comparingInt(EventQueueComboDTO::getWinrate).reversed()))
                .toList();
    }

    private void buildCombinations(List<PlayerMatchRef> refs,
                                   int targetSize,
                                   int index,
                                   List<PlayerMatchRef> current,
                                   Map<String, QueueAccumulator> combos,
                                   Map<String, String> nicknameByPlayerId) {
        if (current.size() == targetSize) {
            List<String> nicknames = current.stream()
                    .map(ref -> nicknameByPlayerId.getOrDefault(ref.playerId(), ref.playerId()))
                    .sorted()
                    .toList();
            String key = String.join("|", nicknames);
            combos.computeIfAbsent(key, ignored -> new QueueAccumulator(nicknames)).add(current);
            return;
        }

        for (int i = index; i <= refs.size() - (targetSize - current.size()); i++) {
            current.add(refs.get(i));
            buildCombinations(refs, targetSize, i + 1, current, combos, nicknameByPlayerId);
            current.remove(current.size() - 1);
        }
    }

    private List<EventMapSummaryDTO> buildEventMapLeaderboard(List<MatchStats> matches) {
        return FaceitLifetimeAggregator.aggregateByMap(matches).stream()
                .map(this::toEventMapSummary)
                .sorted(Comparator.comparingInt(EventMapSummaryDTO::getMatchesPlayed).reversed()
                        .thenComparing(Comparator.comparingInt(EventMapSummaryDTO::getWinrate).reversed()))
                .toList();
    }

    private EventMapSummaryDTO toEventMapSummary(MapStatsDTO mapStats) {
        EventMapSummaryDTO dto = new EventMapSummaryDTO();
        dto.setMap(mapStats.getMap());
        dto.setMatchesPlayed(mapStats.getMatches());
        dto.setWins(mapStats.getWins());
        dto.setLosses(mapStats.getLosses());
        dto.setWinrate(mapStats.getWinrate());
        dto.setAvgKd(mapStats.getAvgKd());
        dto.setAvgKr(mapStats.getAvgKr());
        dto.setAvgAdr(mapStats.getAvgAdr());
        dto.setAvgHsPercent(mapStats.getAvgHsPercent());
        dto.setTotalEloGain(mapStats.getTotalEloGain());
        return dto;
    }

    private List<EventAwardDTO> buildAwards(List<EventPlayerStatsDTO> players) {
        List<EventPlayerStatsDTO> active = players.stream()
                .filter(player -> player.getMatchesPlayed() > 0)
                .toList();

        if (active.isEmpty()) {
            return List.of();
        }

        return List.of(
                award("LAN MVP", maxByAll(active, Comparator.comparingDouble(this::mvpScore)), player -> String.format("%.1f", mvpScore(player)), "Composite score from ELO, K/D, ADR and win rate."),
                award("Elo Farmer", maxByAll(active, Comparator.comparingInt(EventPlayerStatsDTO::getTotalEloGain)), player -> signed(player.getTotalEloGain()), "Highest ELO gain during the event."),
                award("Top Fragger", maxByAll(active, Comparator.comparingDouble(EventPlayerStatsDTO::getAvgKr)), player -> String.format("%.2f", player.getAvgKr()), "Best kill-per-round average."),
                award("Entry Fragger", maxByAll(active, Comparator.comparingDouble(this::entryAttemptScore)), player -> String.format("%.1f%% (%d attempts)", player.getEntryAttemptRate(), player.getEntryCount()), "Highest entry attempt rate."),
                award("Entry Success", maxByAll(active, Comparator.comparingDouble(this::entrySuccessScore)), player -> String.format("%.0f%% (%d attempts)", player.getEntrySuccessRate(), player.getEntryCount()), "Best entry conversion rate with a minimum of three attempts."),
                award("Clutcher", maxByAll(active, Comparator.comparingInt(EventPlayerStatsDTO::getClutchWins)), player -> String.format("%d wins (%d 1v1, %d 1v2)", player.getClutchWins(), player.getClutch1v1Wins(), player.getClutch1v2Wins()), "Most 1v1 and 1v2 clutches won."),
                award("MVP Farmer", maxByAll(active, Comparator.comparingDouble(this::mvpPerGameScore)), player -> String.format("%.2f / game (%d total)", player.getMvpsPerGame(), player.getMvps()), "Highest MVP average per game."),
                award("AWPer", maxByAll(active, Comparator.comparingDouble(this::sniperPerGameScore)), player -> String.format("%.2f / game (%d total)", player.getSniperKillsPerGame(), player.getSniperKills()), "Highest sniper kill average per game."),
                award("Pistol King", maxByAll(active, Comparator.comparingDouble(this::pistolPerGameScore)), player -> String.format("%.2f / game (%d total)", player.getPistolKillsPerGame(), player.getPistolKills()), "Highest pistol kill average per game."),
                award("Multi-kill Machine", maxByAll(active, Comparator.comparingDouble(this::multiKillPerGameScore)), player -> String.format("%.2f / game (%d total)", player.getMultiKillRoundsPerGame(), player.getMultiKillRounds()), "Highest multi-kill rounds per game."),
                award("Flash Support", maxByAll(active, Comparator.comparingDouble(this::flashPerRoundScore)), player -> String.format("%.2f / round (%d total)", player.getEnemiesFlashedPerRound(), player.getEnemiesFlashed()), "Highest enemies flashed per round."),
                award("Nade Damage", maxByAll(active, Comparator.comparingDouble(EventPlayerStatsDTO::getUtilityDamagePerRound)), player -> String.format("%.2f / round", player.getUtilityDamagePerRound()), "Highest utility damage per round."),
                award("Headshot Machine", maxByAll(active, Comparator.comparingDouble(EventPlayerStatsDTO::getAvgHsPercent)), player -> String.format("%.0f%%", player.getAvgHsPercent()), "Highest average headshot percentage."),
                award("Ironman", maxByAll(active, Comparator.comparingInt(EventPlayerStatsDTO::getMatchesPlayed)), player -> player.getMatchesPlayed() + " matches", "Most games played during the LAN."),
                award("Heater Check", maxByAll(active, Comparator.comparingInt(EventPlayerStatsDTO::getLongestWinStreak)), player -> player.getLongestWinStreak() + " in a row", "Longest win streak."),
                award("Tilt Award", maxByAll(active, Comparator.comparingInt(EventPlayerStatsDTO::getLongestLossStreak)), player -> player.getLongestLossStreak() + " losses", "Longest loss streak."),
                award("Baiter", minByAll(active, Comparator.comparingDouble(EventPlayerStatsDTO::getAvgKr)), player -> String.format("%.2f", player.getAvgKr()), "Lowest kill-per-round average.")
        );
    }

    private List<EventPlayerStatsDTO> maxByAll(List<EventPlayerStatsDTO> players, Comparator<EventPlayerStatsDTO> comparator) {
        EventPlayerStatsDTO winner = players.stream().max(comparator).orElse(players.get(0));
        return players.stream()
                .filter(player -> comparator.compare(player, winner) == 0)
                .toList();
    }

    private List<EventPlayerStatsDTO> minByAll(List<EventPlayerStatsDTO> players, Comparator<EventPlayerStatsDTO> comparator) {
        EventPlayerStatsDTO winner = players.stream().min(comparator).orElse(players.get(0));
        return players.stream()
                .filter(player -> comparator.compare(player, winner) == 0)
                .toList();
    }

    private EventAwardDTO award(String title,
                                List<EventPlayerStatsDTO> winners,
                                Function<EventPlayerStatsDTO, String> valueFn,
                                String description) {
        EventPlayerStatsDTO primaryWinner = winners.get(0);
        EventAwardDTO dto = new EventAwardDTO();
        dto.setTitle(title);
        dto.setWinner(winners.stream()
                .map(EventPlayerStatsDTO::getNickname)
                .collect(Collectors.joining(", ")));
        dto.setWinners(winners.stream()
                .map(this::toAwardWinner)
                .toList());
        dto.setValue(valueFn.apply(primaryWinner));
        dto.setDescription(description);
        return dto;
    }

    private EventAwardWinnerDTO toAwardWinner(EventPlayerStatsDTO player) {
        EventAwardWinnerDTO dto = new EventAwardWinnerDTO();
        dto.setPlayerId(player.getPlayerId());
        dto.setNickname(player.getNickname());
        dto.setAvatar(player.getAvatar());
        return dto;
    }

    private double mvpScore(EventPlayerStatsDTO player) {
        return player.getTotalEloGain() * 0.8
                + player.getAvgKd() * 30
                + player.getAvgAdr() * 0.2
                + player.getWinrate() * 0.4;
    }

    private double entrySuccessScore(EventPlayerStatsDTO player) {
        return player.getEntryCount() >= 3 ? player.getEntrySuccessRate() : -1;
    }

    private double entryAttemptScore(EventPlayerStatsDTO player) {
        return player.getEntryCount() >= 3 ? player.getEntryAttemptRate() : -1;
    }

    private double mvpPerGameScore(EventPlayerStatsDTO player) {
        return player.getMatchesPlayed() > 0 ? player.getMvpsPerGame() : -1;
    }

    private double sniperPerGameScore(EventPlayerStatsDTO player) {
        return player.getMatchesPlayed() > 0 ? player.getSniperKillsPerGame() : -1;
    }

    private double pistolPerGameScore(EventPlayerStatsDTO player) {
        return player.getMatchesPlayed() > 0 ? player.getPistolKillsPerGame() : -1;
    }

    private int multiKillScore(EventPlayerStatsDTO player) {
        return player.getDoubleKills()
                + player.getTripleKills() * 2
                + player.getQuadroKills() * 3
                + player.getPentaKills() * 5;
    }

    private double multiKillPerGameScore(EventPlayerStatsDTO player) {
        return player.getMatchesPlayed() > 0 ? player.getMultiKillRoundsPerGame() : -1;
    }

    private double flashPerRoundScore(EventPlayerStatsDTO player) {
        return player.getRoundsPlayed() > 0 ? player.getEnemiesFlashedPerRound() : -1;
    }

    private int winrate(int wins, int totalMatches) {
        return totalMatches > 0 ? (int) Math.round((double) wins / totalMatches * 100) : 0;
    }

    private String signed(int value) {
        return value > 0 ? "+" + value : String.valueOf(value);
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }


    private record CacheEntry(GogoLanEventDTO dto, long expiresAtMillis) {}

    private record MatchStatsCacheEntry(FaceitV4MatchStatsResponse response, long expiresAtMillis) {}

    private record PlayerMatchRef(String playerId, MatchStats match) {}

    private Map<String, AdvancedStatsAccumulator> buildAdvancedStats(Map<String, List<MatchStats>> matchesByPlayer) {
        Map<String, MatchStats> uniqueMatches = matchesByPlayer.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(MatchStats::getMatchId, Function.identity(), (left, right) -> left));

        Map<String, AdvancedStatsAccumulator> accumulators = new HashMap<>();
        uniqueMatches.keySet().forEach(matchId -> {
            FaceitV4MatchStatsResponse response = fetchV4MatchStats(matchId);
            if (response == null || response.getRounds() == null) {
                return;
            }

            response.getRounds().forEach(round -> {
                int roundsPlayed = parseInt(round.getRoundStats(), "Rounds");
                if (round.getTeams() == null) {
                    return;
                }
                round.getTeams().forEach(team -> {
                    if (team.getPlayers() == null) {
                        return;
                    }
                    team.getPlayers().forEach(player -> {
                        if (!properties.getPlayerIds().contains(player.getPlayerId())) {
                            return;
                        }
                        accumulators
                                .computeIfAbsent(player.getPlayerId(), ignored -> new AdvancedStatsAccumulator())
                                .add(player.getPlayerStats(), roundsPlayed);
                    });
                });
            });
        });

        return accumulators;
    }

    private void applyAdvancedStats(EventPlayerStatsDTO player, AdvancedStatsAccumulator accumulator) {
        if (accumulator == null) {
            return;
        }

        player.setEntryCount(accumulator.entryCount);
        player.setEntryWins(accumulator.entryWins);
        player.setEntrySuccessRate(accumulator.entryCount > 0 ? round((double) accumulator.entryWins / accumulator.entryCount * 100.0) : 0);
        player.setEntryAttemptRate(accumulator.roundsPlayed > 0 ? round((double) accumulator.entryCount / accumulator.roundsPlayed * 100.0) : 0);
        player.setClutch1v1Wins(accumulator.clutch1v1Wins);
        player.setClutch1v2Wins(accumulator.clutch1v2Wins);
        player.setClutchWins(accumulator.clutch1v1Wins + accumulator.clutch1v2Wins);
        player.setClutchKills(accumulator.clutchKills);
        player.setMvps(accumulator.mvps);
        player.setMvpsPerGame(player.getMatchesPlayed() > 0 ? round((double) accumulator.mvps / player.getMatchesPlayed()) : 0);
        player.setSniperKills(accumulator.sniperKills);
        player.setSniperKillsPerGame(player.getMatchesPlayed() > 0 ? round((double) accumulator.sniperKills / player.getMatchesPlayed()) : 0);
        player.setPistolKills(accumulator.pistolKills);
        player.setPistolKillsPerGame(player.getMatchesPlayed() > 0 ? round((double) accumulator.pistolKills / player.getMatchesPlayed()) : 0);
        player.setDoubleKills(accumulator.doubleKills);
        player.setTripleKills(accumulator.tripleKills);
        player.setQuadroKills(accumulator.quadroKills);
        player.setPentaKills(accumulator.pentaKills);
        player.setMultiKillRounds(accumulator.doubleKills + accumulator.tripleKills + accumulator.quadroKills + accumulator.pentaKills);
        player.setMultiKillRoundsPerGame(player.getMatchesPlayed() > 0 ? round((double) player.getMultiKillRounds() / player.getMatchesPlayed()) : 0);
        player.setFlashSuccesses(accumulator.flashSuccesses);
        player.setEnemiesFlashed(accumulator.enemiesFlashed);
        player.setEnemiesFlashedPerRound(accumulator.roundsPlayed > 0 ? round((double) accumulator.enemiesFlashed / accumulator.roundsPlayed) : 0);
        player.setFlashSuccessRate(accumulator.flashCount > 0 ? round((double) accumulator.flashSuccesses / accumulator.flashCount * 100.0) : 0);
        player.setUtilityDamage(accumulator.utilityDamage);
        player.setUtilitySuccesses(accumulator.utilitySuccesses);
        player.setUtilityCount(accumulator.utilityCount);
        player.setRoundsPlayed(accumulator.roundsPlayed);
        player.setUtilityDamagePerRound(accumulator.roundsPlayed > 0 ? round((double) accumulator.utilityDamage / accumulator.roundsPlayed) : 0);
    }

    private int parseInt(Map<String, String> stats, String key) {
        if (stats == null) {
            return 0;
        }
        try {
            return Integer.parseInt(stats.getOrDefault(key, "0"));
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static class QueueAccumulator {
        private final List<String> nicknames;
        private int matchesPlayed;
        private int wins;
        private int totalEloGain;

        private QueueAccumulator(List<String> nicknames) {
            this.nicknames = nicknames;
        }

        private void add(List<PlayerMatchRef> refs) {
            matchesPlayed++;
            if ("1".equals(refs.get(0).match().getStats().getResult())) {
                wins++;
            }

            int avgEloGain = (int) Math.round(refs.stream()
                    .map(ref -> ref.match().getEloGain())
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0));
            totalEloGain += avgEloGain;
        }

        private EventQueueComboDTO toDto() {
            EventQueueComboDTO dto = new EventQueueComboDTO();
            dto.setPlayerNicknames(nicknames);
            dto.setLineupSize(nicknames.size());
            dto.setMatchesPlayed(matchesPlayed);
            dto.setWins(wins);
            dto.setLosses(matchesPlayed - wins);
            dto.setWinrate(matchesPlayed > 0 ? (int) Math.round((double) wins / matchesPlayed * 100) : 0);
            dto.setTotalEloGain(totalEloGain);
            return dto;
        }
    }

    private class AdvancedStatsAccumulator {
        private int entryCount;
        private int entryWins;
        private int clutch1v1Wins;
        private int clutch1v2Wins;
        private int clutchKills;
        private int mvps;
        private int sniperKills;
        private int pistolKills;
        private int doubleKills;
        private int tripleKills;
        private int quadroKills;
        private int pentaKills;
        private int flashCount;
        private int flashSuccesses;
        private int enemiesFlashed;
        private int utilityDamage;
        private int utilitySuccesses;
        private int utilityCount;
        private int roundsPlayed;

        private void add(Map<String, String> stats, int roundCount) {
            entryCount += parseInt(stats, "Entry Count");
            entryWins += parseInt(stats, "Entry Wins");
            clutch1v1Wins += parseInt(stats, "1v1Wins");
            clutch1v2Wins += parseInt(stats, "1v2Wins");
            clutchKills += parseInt(stats, "Clutch Kills");
            mvps += parseInt(stats, "MVPs");
            sniperKills += parseInt(stats, "Sniper Kills");
            pistolKills += parseInt(stats, "Pistol Kills");
            doubleKills += parseInt(stats, "Double Kills");
            tripleKills += parseInt(stats, "Triple Kills");
            quadroKills += parseInt(stats, "Quadro Kills");
            pentaKills += parseInt(stats, "Penta Kills");
            flashCount += parseInt(stats, "Flash Count");
            flashSuccesses += parseInt(stats, "Flash Successes");
            enemiesFlashed += parseInt(stats, "Enemies Flashed");
            utilityDamage += parseInt(stats, "Utility Damage");
            utilitySuccesses += parseInt(stats, "Utility Successes");
            utilityCount += parseInt(stats, "Utility Count");
            roundsPlayed += roundCount;
        }
    }
}





