package com.faceit.backend.service;

import com.faceit.backend.dto.FaceitPlayerSuggestionDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FaceitPlayerSearchService {
    private final WebClient authenticatedWebClient;

    public FaceitPlayerSearchService(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.authenticatedWebClient = authenticatedWebClient;
    }

    public List<FaceitPlayerSuggestionDTO> searchPlayers(String query) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        JsonNode response = authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/v4/search/players")
                        .queryParam("nickname", query.trim())
                        .queryParam("limit", 8)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            return List.of();
        }

        JsonNode items = response.has("items") ? response.get("items") : response;
        if (items == null || !items.isArray()) {
            return List.of();
        }

        List<FaceitPlayerSuggestionDTO> suggestions = new ArrayList<>();
        for (JsonNode item : items) {
            if (item == null || item.isNull()) {
                continue;
            }
            FaceitPlayerSuggestionDTO dto = new FaceitPlayerSuggestionDTO();
            dto.setPlayerId(item.path("player_id").asText(""));
            dto.setNickname(item.path("nickname").asText(""));
            dto.setAvatar(item.path("avatar").asText(null));
            dto.setCountry(item.path("country").asText(""));
            dto.setSkillLevel(item.path("games").path("cs2").path("skill_level").asInt(0));
            dto.setFaceitElo(item.path("games").path("cs2").path("faceit_elo").asInt(0));
            if (!dto.getPlayerId().isBlank() && !dto.getNickname().isBlank()) {
                suggestions.add(dto);
            }
        }
        return suggestions;
    }
}
