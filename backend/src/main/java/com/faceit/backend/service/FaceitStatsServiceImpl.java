package com.faceit.backend.service;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FaceitStatsServiceImpl implements FaceitStatsService {

    private final WebClient webClient;

    public FaceitStatsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.faceit.com").build();
    }

    @Override
    public PlayerStatsDTO getLast30Stats(String playerId) {
        // Fetch last 30 matches
        List<FaceitMatchStatsResponse.MatchStats> matchList = webClient.get()
                .uri("/api/stats/v1/stats/time/users/{playerId}/games/cs2?size=30&game_mode=5v5", playerId)
                .retrieve()
                .bodyToFlux(FaceitMatchStatsResponse.MatchStats.class)
                .collectList()
                .block();

        // Extract ELO history from the matches (from "i1")
        List<Integer> eloHistory = new ArrayList<>();
        if (matchList != null) {
            for (FaceitMatchStatsResponse.MatchStats match : matchList) {
                try {
                    int elo = Integer.parseInt(match.getElo());
                    eloHistory.add(elo);
                } catch (NumberFormatException ignored) {}
            }
        }

        return PlayerStatsDTO.fromMatches(matchList, playerId, eloHistory);
    }

    @Override
    public List<PlayerStatsDTO> getStatsForPlayers(List<String> playerIds) {
        List<PlayerStatsDTO> results = new ArrayList<>();
        for (String id : playerIds) {
            results.add(getLast30Stats(id));
        }
        return results;
    }
}
