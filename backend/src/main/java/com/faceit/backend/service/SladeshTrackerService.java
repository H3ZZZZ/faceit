package com.faceit.backend.service;

import com.faceit.backend.dto.SladeshTrackerDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class SladeshTrackerService {

    private final WebClient webClient;
    private final WebClient authenticatedWebClient;

    private SladeshTrackerDTO cachedResult = null;
    private long cacheExpiryTime = 0;


    public SladeshTrackerService(@Qualifier("unauthenticatedWebClient") WebClient webClient,
                                 @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.webClient = webClient;
        this.authenticatedWebClient = authenticatedWebClient;
    }

    public SladeshTrackerDTO getSladeshTrackerData() {
        long now = System.currentTimeMillis();
        if (cachedResult != null && now < cacheExpiryTime) {
            return cachedResult;
        }

        FaceitPlayerInfo info = fetchPlayerInfoByNickname("sladesh");
        if (info == null) {
            throw new RuntimeException("Could not fetch Sladesh info.");
        }

        List<MatchStats> matches = fetchAllCs2Matches(info.getPlayerId());

        Long lastTimeLevel10 = null;
        String matchIdLevel10 = null;
        Long lastTimeDroppedBelow10 = null;
        String matchIdDroppedBelow10 = null;

        Integer prevElo = null;

        for (MatchStats match : matches) {
            Integer elo = parseElo(match.getElo());
            if (elo == null) continue;

            // We check transition from previous match → this match
            if (prevElo != null) {
                // Transition into level 10 (from <2001 to ≥2001)
                if (prevElo < 2001 && elo >= 2001 && lastTimeLevel10 == null) {
                    lastTimeLevel10 = match.getDate();
                    matchIdLevel10 = match.getMatchId();
                }

                // Transition out of level 10 (from ≥2001 to <2001)
                if (prevElo >= 2001 && elo < 2001 && lastTimeDroppedBelow10 == null) {
                    lastTimeDroppedBelow10 = match.getDate();
                    matchIdDroppedBelow10 = match.getMatchId();
                }

                // Stop early if both found
                if (lastTimeLevel10 != null && lastTimeDroppedBelow10 != null) {
                    break;
                }
            }

            prevElo = elo;
        }




        SladeshTrackerDTO dto = new SladeshTrackerDTO(
                info.getNickname(),
                info.getFaceitElo(),
                info.getSkillLevel(),
                lastTimeLevel10,
                matchIdLevel10,
                lastTimeDroppedBelow10,
                matchIdDroppedBelow10
        );

        cachedResult = dto;
        cacheExpiryTime = now + (5 * 60 * 1000); // Cache for 5 minutes
        return dto;
    }


    private List<MatchStats> fetchAllCs2Matches(String playerId) {
        List<MatchStats> allMatches = new ArrayList<>();
        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";
        Long to = null;
        boolean more = true;

        while (more && allMatches.size() < 500) {
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
                if (page.size() < 100 || allMatches.size() >= 500) {
                    more = false;
                } else {
                    long lastMatchTimestamp = page.get(page.size() - 1).getDate();
                    to = lastMatchTimestamp - 1;
                }
            }
        }

        return allMatches;
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

    private static Integer parseElo(String elo) {
        try {
            return elo != null ? Integer.parseInt(elo) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
