package com.faceit.backend.service;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.model.FaceitPlayerInfo;
import com.faceit.backend.model.FaceitV4PlayerGameStatsResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FaceitStatsV4Service {

    private final WebClient authenticatedWebClient;

    public FaceitStatsV4Service(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.authenticatedWebClient = authenticatedWebClient;
    }

    public PlayerStatsDTO getStatsForPlayer(String playerId) {
        FaceitV4PlayerGameStatsResponse response = authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/v4/players/{playerId}/games/cs2/stats")
                        .queryParam("limit", 100)
                        .queryParam("offset", 0)
                        .build(playerId))
                .retrieve()
                .bodyToMono(FaceitV4PlayerGameStatsResponse.class)
                .block();

        List<FaceitV4PlayerGameStatsResponse.Item> items =
                response != null && response.getItems() != null ? response.getItems() : List.of();

        PlayerStatsDTO dto = PlayerStatsDTO.fromV4Matches(items, playerId);

        FaceitPlayerInfo playerInfo = authenticatedWebClient.get()
                .uri("/data/v4/players/{playerId}", playerId)
                .retrieve()
                .bodyToMono(FaceitPlayerInfo.class)
                .block();

        if (playerInfo != null) {
            dto.setAvatar(playerInfo.getAvatar());
            dto.setCountry(playerInfo.getCountry());
            dto.setSkillLevel(playerInfo.getSkillLevel());
            dto.setFaceitElo(playerInfo.getFaceitElo());
        }

        return dto;
    }

    public List<PlayerStatsDTO> getStatsForPlayers(List<String> playerIds) {
        List<PlayerStatsDTO> results = new ArrayList<>();
        for (String playerId : playerIds) {
            results.add(getStatsForPlayer(playerId));
        }
        return results;
    }
}
