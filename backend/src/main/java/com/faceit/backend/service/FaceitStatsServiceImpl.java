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
        // Fetch last 30 matches
        List<FaceitMatchStatsResponse.MatchStats> matchList = webClient.get()
                .uri("/api/stats/v1/stats/time/users/{playerId}/games/cs2?size=30&game_mode=5v5", playerId)
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

        // Fetch player info from authenticated API
        FaceitPlayerInfo playerInfo = fetchPlayerInfo(playerId);

        // Build stats DTO and enrich with profile info
        PlayerStatsDTO dto = PlayerStatsDTO.fromMatches(matchList, playerId, eloHistory);
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
