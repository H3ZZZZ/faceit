package com.faceit.backend.service;

import com.faceit.backend.config.FaceitConfig;
import com.faceit.backend.dto.Matchroom;
import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.dto.PlayerStatsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class FaceitStatsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FaceitConfig faceitConfig;

    public FaceitStatsService(FaceitConfig faceitConfig) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.faceitConfig = faceitConfig;
    }

    public PlayerStatsResponse getPlayerStats(String playerId) throws Exception {
        String url = String.format("https://api.faceit.com/stats/v1/stats/time/users/%s/games/cs2", playerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + faceitConfig.getApiKey());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        List<Matchroom> allMatches = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

        if (allMatches.size() < 100) {
            throw new IllegalStateException("Not enough matches returned");
        }

        String nickname = allMatches.get(0).getNickname();

        return new PlayerStatsResponse(
                playerId,
                nickname,
                buildStats(allMatches, 10),
                buildStats(allMatches, 30),
                buildStats(allMatches, 50),
                buildStats(allMatches, 99)
        );
    }

    private PlayerStatsDTO buildStats(List<Matchroom> allGames, int limit) {
        if (allGames.size() <= limit) return new PlayerStatsDTO();

        List<Matchroom> games = allGames.subList(0, limit);
        Matchroom latest = games.get(0); // Nyeste kamp (første i listen)

        // Find første kamp der har et gyldigt elo-felt (kan være vi spiller uden ELO nogle gange)
        Matchroom oldestWithElo = null;
        for (int i = limit; i < allGames.size(); i++) {
            if (allGames.get(i).getElo() != null) {
                oldestWithElo = allGames.get(i);
                break;
            }
        }

        int eloDiff = (oldestWithElo != null && latest.getElo() != null)
                ? latest.getElo() - oldestWithElo.getElo()
                : 0;

        int wins = (int) games.stream().filter(g -> g.getI10() == 1).count();
        int losses = limit - wins;
        int winRate = (int) Math.round((wins * 100.0) / limit);

        int totalKills = games.stream().mapToInt(Matchroom::getI6).sum();
        int totalAssists = games.stream().mapToInt(Matchroom::getI7).sum();
        int totalDeaths = games.stream().mapToInt(Matchroom::getI8).sum();

        double avgKD = round2(games.stream().mapToDouble(Matchroom::getC2).average().orElse(0));
        double avgKR = round2(games.stream().mapToDouble(Matchroom::getC3).average().orElse(0));
        double avgADR = round2(games.stream().mapToDouble(Matchroom::getC10).average().orElse(0));
        int avgHS = (int) Math.round(games.stream().mapToInt(Matchroom::getC4).average().orElse(0));

        return new PlayerStatsDTO(
                limit,
                wins,
                losses,
                winRate,
                eloDiff,
                totalKills / limit,
                totalAssists / limit,
                totalDeaths / limit,
                avgKD,
                avgKR,
                avgADR,
                avgHS
        );
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }



}
