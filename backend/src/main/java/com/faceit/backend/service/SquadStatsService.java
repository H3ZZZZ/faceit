package com.faceit.backend.service;

import com.faceit.backend.dto.Didweplay.SharedPlayerStatsDTO;
import com.faceit.backend.model.FaceitPlayerInfo;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.service.util.SharedStatsAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SquadStatsService {

    private final WebClient authenticatedWebClient;
    private final WebClient unauthenticatedWebClient;

    public SquadStatsService(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient,
                             @Qualifier("unauthenticatedWebClient") WebClient unauthenticatedWebClient) {
        this.authenticatedWebClient = authenticatedWebClient;
        this.unauthenticatedWebClient = unauthenticatedWebClient;
    }

    public List<SharedPlayerStatsDTO> getSharedStats(List<String> nicknames) {
        if (nicknames == null || nicknames.size() < 2) return List.of();

        Map<String, FaceitPlayerInfo> profileMap = new ConcurrentHashMap<>();
        Map<String, List<MatchStats>> matchesMap = new ConcurrentHashMap<>();

        // Fetch profil og alle CS2-kampe for hver spiller
        nicknames.parallelStream().forEach(nickname -> {
            FaceitPlayerInfo profile = fetchPlayerInfoByNickname(nickname);
            if (profile != null) {
                profileMap.put(nickname, profile);
                List<MatchStats> matches = fetchAllCs2Matches(profile.getPlayerId());
                SharedStatsAggregator.calculateEloGain(matches);
                matchesMap.put(nickname, matches);
            }
        });

        // Hvis vi ikke kunne hente stats for alle
        if (profileMap.size() != nicknames.size()) return List.of();

        // Find fælles {matchId|teamId} par for alle spillere
        Map<String, Set<String>> playerMatchKeys = new HashMap<>();

        for (String nickname : nicknames) {
            Set<String> keys = matchesMap.get(nickname).stream()
                    .map(m -> m.getMatchId() + "|" + m.getTeamId())
                    .collect(Collectors.toSet());
            playerMatchKeys.put(nickname, keys);
        }

        // Find snittet af alle sets = fælles kampe på samme hold
        Set<String> sharedMatchKeys = playerMatchKeys.values().stream()
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                })
                .orElse(Set.of());

        if (sharedMatchKeys.isEmpty()) return List.of();

        List<SharedPlayerStatsDTO> results = new ArrayList<>();

        for (String nickname : nicknames) {
            FaceitPlayerInfo profile = profileMap.get(nickname);
            List<MatchStats> sharedMatches = matchesMap.get(nickname).stream()
                    .filter(m -> sharedMatchKeys.contains(m.getMatchId() + "|" + m.getTeamId()))
                    .sorted(Comparator.comparing(MatchStats::getDate).reversed())
                    .toList();

            SharedPlayerStatsDTO dto = SharedStatsAggregator.aggregateSharedStats(profile, sharedMatches);
            results.add(dto);
        }

        return results;
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

    private List<MatchStats> fetchAllCs2Matches(String playerId) {
        String baseUrl = "/api/stats/v1/stats/time/users/" + playerId + "/games/cs2?size=100&game_mode=5v5";

        Mono<List<MatchStats>> firstPageMono = unauthenticatedWebClient.get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(MatchStats.class)
                .collectList();

        List<MatchStats> firstPage = firstPageMono.block();
        if (firstPage == null || firstPage.isEmpty()) return new ArrayList<>();

        List<Long> timestamps = new ArrayList<>();
        long lastTo = firstPage.get(firstPage.size() - 1).getDate() - 1;

        while (true) {
            timestamps.add(lastTo);
            String url = baseUrl + "&to=" + lastTo;

            List<MatchStats> testPage = unauthenticatedWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(MatchStats.class)
                    .collectList()
                    .block();

            if (testPage == null || testPage.isEmpty() || testPage.size() < 100) {
                break;
            }

            lastTo = testPage.get(testPage.size() - 1).getDate() - 1;
        }

        List<Mono<List<MatchStats>>> parallelCalls = timestamps.stream()
                .map(to -> {
                    String url = baseUrl + "&to=" + to;
                    return unauthenticatedWebClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToFlux(MatchStats.class)
                            .collectList();
                })
                .collect(Collectors.toList());

        parallelCalls.add(0, firstPageMono);

        List<MatchStats> allMatches = Flux.merge(parallelCalls)
                .flatMap(Flux::fromIterable)
                .collectList()
                .block();

        if (allMatches != null) {
            allMatches.sort(Comparator.comparing(MatchStats::getDate).reversed());
        }

        return allMatches != null ? allMatches : new ArrayList<>();
    }
}
