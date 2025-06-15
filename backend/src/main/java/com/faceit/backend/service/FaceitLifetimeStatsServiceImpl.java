package com.faceit.backend.service;

import com.faceit.backend.dto.LifetimeStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;
import com.faceit.backend.service.util.FaceitLifetimeAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaceitLifetimeStatsServiceImpl implements FaceitLifetimeStatsService {

    private final WebClient webClient;
    private final WebClient authenticatedWebClient;

    public FaceitLifetimeStatsServiceImpl(@Qualifier("unauthenticatedWebClient") WebClient webClient,
                                          @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.webClient = webClient;
        this.authenticatedWebClient = authenticatedWebClient;
    }
    private FaceitPlayerInfo fetchPlayerInfoByNickname(String nickname) {
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/v4/players")
                        .queryParam("nickname", nickname)
                        .build())
                .retrieve()
                .bodyToMono(FaceitPlayerInfo.class)
                .block();
    }

    @Override
    public LifetimeStatsDTO getLifetimeStatsByNickname(String nickname) {
        FaceitPlayerInfo info = fetchPlayerInfoByNickname(nickname);
        if (info == null) {
            throw new RuntimeException("No player found with nickname: " + nickname);
        }

        String playerId = info.getPlayerId(); // Du skal tilføje dette felt i FaceitPlayerInfo

        LifetimeStatsDTO dto = getLifetimeStats(playerId);

        // Berig DTO med profiloplysninger
        dto.setAvatar(info.getAvatar());
        dto.setCountry(info.getCountry());
        dto.setSkillLevel(info.getSkillLevel());
        dto.setFaceitElo(info.getFaceitElo());
        dto.setGame_player_id(info.getGamePlayerId()); // Tilføj game_player_id til DTO
        dto.setNickname(nickname); // Tilføj nickname til DTO

        return dto;
    }

    // DEN GAMLE METODE DER VAR LANGSOMMERE
//    @Override
//    public List<MatchStats> fetchAllCs2Matches(String playerId) {
//        List<MatchStats> allMatches = new ArrayList<>();
//        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";
//        Long to = null;
//        boolean more = true;
//
//        while (more) {
//            String url = baseUrl + (to != null ? "&to=" + to : "");
//
//            List<MatchStats> page = webClient.get()
//                    .uri(url)
//                    .retrieve()
//                    .bodyToFlux(MatchStats.class)
//                    .collectList()
//                    .block();
//
//            if (page == null || page.isEmpty()) {
//                more = false;
//            } else {
//                allMatches.addAll(page);
//                if (page.size() < 100) {
//                    more = false;
//                } else {
//                    long lastMatchTimestamp = page.get(page.size() - 1).getDate();
//                    to = lastMatchTimestamp - 1;
//                }
//            }
//        }
//
//        return allMatches;
//    }

    @Override
    public List<MatchStats> fetchAllCs2Matches(String playerId) {
        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";

        // Step 1: Fetch the first (latest) page
        Mono<List<MatchStats>> firstPageMono = webClient.get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(MatchStats.class)
                .collectList();

        List<MatchStats> firstPage = firstPageMono.block();
        if (firstPage == null || firstPage.isEmpty()) return new ArrayList<>();

        // Step 2: Collect all 'to' timestamps by walking backward
        List<Long> timestamps = new ArrayList<>();
        long lastTo = firstPage.get(firstPage.size() - 1).getDate() - 1;

        while (true) {
            timestamps.add(lastTo);
            String url = baseUrl + "&to=" + lastTo;

            List<MatchStats> testPage = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(MatchStats.class)
                    .collectList()
                    .block();

            if (testPage == null || testPage.isEmpty() || testPage.size() < 100) {
                break;
            }

            lastTo = testPage.get(testPage.size() - 1).getDate() - 1;
        }

        // Step 3: Create parallel Monos for all pages
        List<Mono<List<MatchStats>>> parallelCalls = timestamps.stream()
                .map(to -> {
                    String url = baseUrl + "&to=" + to;
                    return webClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToFlux(MatchStats.class)
                            .collectList();
                })
                .collect(Collectors.toList());

        // Include first page in parallel merge
        parallelCalls.add(0, firstPageMono);

        // Step 4: Run all requests in parallel and flatten result
        List<MatchStats> allMatches = Flux.merge(parallelCalls)
                .flatMap(Flux::fromIterable)
                .collectList()
                .block();

        if (allMatches != null) {
            allMatches.sort(Comparator.comparing(MatchStats::getDate).reversed());
        }


        return allMatches != null ? allMatches : new ArrayList<>();
    }



    @Override
    public LifetimeStatsDTO getLifetimeStats(String playerId) {
        List<MatchStats> allMatches = fetchAllCs2Matches(playerId);
        FaceitLifetimeAggregator.calculateEloGain(allMatches);

        var stats = FaceitLifetimeAggregator.aggregate(allMatches);

        LifetimeStatsDTO dto = new LifetimeStatsDTO();

        dto.setMatchesPlayed(stats.matchesPlayed());
        dto.setTotalWins(stats.totalWins());
        dto.setTotalLosses(stats.totalLosses());
        dto.setEloChange(stats.eloChange());
        dto.setAvgKd(stats.avgKd());
        dto.setAvgKr(stats.avgKr());
        dto.setAvgAdr(stats.avgAdr());
        dto.setAvgHsPercent(stats.avgHsPercent());
        dto.setMostKills(stats.mostKills());
        dto.setMostKillsMatchId(stats.mostKillsMatchId());
        dto.setFewestKills(stats.fewestKills());
        dto.setFewestKillsMatchId(stats.fewestKillsMatchId());
        dto.setLongestWinStreak(stats.longestWinStreak());
        dto.setLongestLossStreak(stats.longestLossStreak());
        dto.setHighestKr(stats.highestKr());
        dto.setHighestKrMatchId(stats.highestKrMatchId());
        dto.setLowestKr(stats.lowestKr());
        dto.setLowestKrMatchId(stats.lowestKrMatchId());
        dto.setHighestKd(stats.highestKd());
        dto.setHighestKdMatchId(stats.highestKdMatchId());
        dto.setLowestKd(stats.lowestKd());
        dto.setLowestKdMatchId(stats.lowestKdMatchId());
        dto.setTotalEloGain(stats.totalEloGain());
        dto.setMapStats(FaceitLifetimeAggregator.aggregateByMap(allMatches));
        int winrate = (stats.matchesPlayed() > 0)
                ? (int) Math.round((double) stats.totalWins() / stats.matchesPlayed() * 100)
                : 0;
        dto.setWinrate(winrate);
        dto.setKavg(stats.kavg());
        dto.setAavg(stats.aavg());
        dto.setDavg(stats.davg());
        dto.setHighestElo(stats.highestElo());
        dto.setLowestElo(stats.lowestElo());
        dto.setLast5Results(stats.last5Results());
        return dto;
    }


}
