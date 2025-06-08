package com.faceit.backend.service;

import com.faceit.backend.dto.LifetimeStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.service.util.FaceitLifetimeAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FaceitLifetimeStatsServiceImpl implements FaceitLifetimeStatsService {

    private final WebClient webClient;

    public FaceitLifetimeStatsServiceImpl(@Qualifier("unauthenticatedWebClient") WebClient webClient) {
        this.webClient = webClient;
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


        return dto;
    }
}
