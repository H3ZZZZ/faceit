package com.faceit.backend.service;

import com.faceit.backend.dto.LifetimeStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;
import com.faceit.backend.service.util.FaceitLifetimeAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<MatchStats> fetchAllCs2Matches(String playerId) {
        List<MatchStats> allMatches = new ArrayList<>();
        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";
        Long to = null;
        boolean more = true;

        while (more) {
            String url = baseUrl + (to != null ? "&to=" + to : "");

            List<MatchStats> page = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(MatchStats.class)
                    .collectList()
                    .block();

            if (page == null || page.isEmpty()) {
                more = false;
            } else {
                allMatches.addAll(page);
                if (page.size() < 100) {
                    more = false;
                } else {
                    long lastMatchTimestamp = page.get(page.size() - 1).getDate();
                    to = lastMatchTimestamp - 1;
                }
            }
        }

        return allMatches;
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
        dto.setLast5Results(stats.last5Results());
        return dto;
    }


}
