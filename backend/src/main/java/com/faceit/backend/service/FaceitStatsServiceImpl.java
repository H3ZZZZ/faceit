package com.faceit.backend.service;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse;
import com.faceit.backend.model.FaceitPlayerInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FaceitStatsServiceImpl implements FaceitStatsService {

    private final WebClient webClient;               // for v1 (no token)
    private final WebClient authenticatedWebClient;  // for v4 (token)

    public FaceitStatsServiceImpl(@Qualifier("unauthenticatedWebClient") WebClient webClient,
                                  @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.webClient = webClient;
        this.authenticatedWebClient = authenticatedWebClient;
    }

    private FaceitPlayerInfo fetchPlayerInfo(String playerId) {
        return authenticatedWebClient.get()
                .uri("/data/v4/players/{playerId}", playerId)
                .retrieve()
                .bodyToMono(FaceitPlayerInfo.class)
                .block();
    }

    @Override
    public PlayerStatsDTO getLast30Stats(String playerId) {
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

        List<String> last5Results = new ArrayList<>();
        for (int i = 0; i < Math.min(5, matchList.size()); i++) {
            try {
                boolean won = "1".equals(matchList.get(i).getStats().getResult());
                last5Results.add(won ? "W" : "L");
            } catch (Exception ignored) {}
        }
        Collections.reverse(last5Results);

        FaceitPlayerInfo playerInfo = fetchPlayerInfo(playerId);

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
