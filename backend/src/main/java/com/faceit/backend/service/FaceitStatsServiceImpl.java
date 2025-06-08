package com.faceit.backend.service;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse;
import com.faceit.backend.model.FaceitPlayerInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;


import java.util.ArrayList;
import java.util.List;

@Service
public class FaceitStatsServiceImpl implements FaceitStatsService {

    private final WebClient webClient;
    private final String faceitApiKey;

    public FaceitStatsServiceImpl(WebClient.Builder webClientBuilder,
                                  @Value("${faceit.api.key}") String faceitApiKey) {
        this.webClient = webClientBuilder.baseUrl("https://www.faceit.com").build();
        this.faceitApiKey = faceitApiKey;
    }

    private FaceitPlayerInfo fetchPlayerInfo(String playerId) {
        return WebClient.builder()
                .baseUrl("https://open.faceit.com")
                .defaultHeader("Authorization", "Bearer " + faceitApiKey)
                .build()
                .get()
                .uri("/data/v4/players/{playerId}", playerId)
                .retrieve()
                .bodyToMono(FaceitPlayerInfo.class)
                .block();
    }



    @Override
    public PlayerStatsDTO getLast30Stats(String playerId) {
        // Fetch last 100 matches
        List<FaceitMatchStatsResponse.MatchStats> matchList = webClient.get()
                .uri("/api/stats/v1/stats/time/users/{playerId}/games/cs2?size=100&game_mode=5v5", playerId)
                .retrieve()
                .bodyToFlux(FaceitMatchStatsResponse.MatchStats.class)
                .collectList()
                .block();

        List<Integer> eloHistory = new ArrayList<>();
        if (matchList != null) {
            for (FaceitMatchStatsResponse.MatchStats match : matchList) {
                try {
                    int elo = Integer.parseInt(match.getElo());
                    eloHistory.add(elo);
                } catch (NumberFormatException ignored) {}
            }
        }

// Extract last5Results from most recent matches (top of list)
        List<String> last5Results = new ArrayList<>();
        for (int i = 0; i < Math.min(5, matchList.size()); i++) {
            try {
                boolean won = "1".equals(matchList.get(i).getStats().getResult());
                last5Results.add(won ? "W" : "L");
            } catch (Exception ignored) {}
        }
        java.util.Collections.reverse(last5Results);

        // Fetch player info
        FaceitPlayerInfo playerInfo = fetchPlayerInfo(playerId);

        // 🧠 Build DTO using the new helper that passes `last5Results`
        PlayerStatsDTO dto = PlayerStatsDTO.fromMatches(matchList, playerId, eloHistory, last5Results);
        if (playerInfo != null) {
            dto.setAvatar(playerInfo.getAvatar());
            dto.setCountry(playerInfo.getCountry());
            dto.setSkillLevel(playerInfo.getSkillLevel());
            dto.setFaceitElo(playerInfo.getFaceitElo());
        }

        return dto;
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
