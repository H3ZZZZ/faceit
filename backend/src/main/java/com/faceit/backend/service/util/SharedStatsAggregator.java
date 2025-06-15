package com.faceit.backend.service.util;

import com.faceit.backend.dto.Didweplay.SharedPlayerStatsDTO;
import com.faceit.backend.dto.Didweplay.SharedPlayerMapStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;

import java.util.*;
import java.util.stream.Collectors;

public class SharedStatsAggregator {

    public static SharedPlayerStatsDTO aggregateSharedStats(FaceitPlayerInfo profile, List<MatchStats> matches) {
        int total = matches.size();
        int wins = (int) matches.stream().filter(SharedStatsAggregator::isWin).count();
        int losses = total - wins;
        int eloGain = matches.stream().mapToInt(m -> Optional.ofNullable(m.getEloGain()).orElse(0)).sum();

        double avgKd = average(matches, m -> parseDoubleSafe(m.getStats().getKdRatio()));
        double avgKr = average(matches, m -> parseDoubleSafe(m.getStats().getKrRatio()));
        double avgAdr = average(matches, m -> parseDoubleSafe(m.getStats().getAdr()));
        double avgHs = average(matches, m -> parseDoubleSafe(m.getStats().getHeadshotsPercent()));

        Map<String, List<MatchStats>> matchesByMap = matches.stream()
                .collect(Collectors.groupingBy(m -> m.getStats().getMap()));

        List<SharedPlayerMapStatsDTO> mapStats = new ArrayList<>();
        for (Map.Entry<String, List<MatchStats>> entry : matchesByMap.entrySet()) {
            String map = entry.getKey();
            List<MatchStats> mapMatches = entry.getValue();
            int mapWins = (int) mapMatches.stream().filter(SharedStatsAggregator::isWin).count();
            int mapLosses = mapMatches.size() - mapWins;

            SharedPlayerMapStatsDTO mapDto = new SharedPlayerMapStatsDTO();
            mapDto.setMap(map);
            mapDto.setMatches(mapMatches.size());
            mapDto.setWins(mapWins);
            mapDto.setLosses(mapLosses);
            mapDto.setWinrate((int) Math.round(mapWins * 100.0 / mapMatches.size()));
            mapDto.setAvgKd(round(average(mapMatches, m -> parseDoubleSafe(m.getStats().getKdRatio())), 2));
            mapDto.setAvgKr(round(average(mapMatches, m -> parseDoubleSafe(m.getStats().getKrRatio())), 2));
            mapDto.setAvgAdr(round(average(mapMatches, m -> parseDoubleSafe(m.getStats().getAdr())), 2));
            mapDto.setAvgHsPercent((int) Math.round(average(mapMatches, m -> parseDoubleSafe(m.getStats().getHeadshotsPercent()))));
            mapDto.setTotalEloGain(mapMatches.stream().mapToInt(m -> Optional.ofNullable(m.getEloGain()).orElse(0)).sum());
            mapStats.add(mapDto);
        }

        SharedPlayerStatsDTO dto = new SharedPlayerStatsDTO();
        dto.setNickname(profile.getNickname());
        dto.setAvatar(profile.getAvatar());
        dto.setSkillLevel(profile.getSkillLevel());
        dto.setFaceitElo(profile.getFaceitElo());
        dto.setTotalMatches(total);
        dto.setWins(wins);
        dto.setLosses(losses);
        dto.setWinrate((int) Math.round(wins * 100.0 / total));
        dto.setAvgKd(round(avgKd, 2));
        dto.setAvgKr(round(avgKr, 2));
        dto.setAvgAdr(round(avgAdr, 2));
        dto.setAvgHsPercent((int) Math.round(avgHs));
        dto.setTotalEloGain(eloGain);
        dto.setMapStats(mapStats);

        return dto;
    }

    private static boolean isWin(MatchStats m) {
        return "1".equals(m.getStats().getResult());
    }

    private static double average(List<MatchStats> matches, java.util.function.ToDoubleFunction<MatchStats> extractor) {
        return matches.stream()
                .mapToDouble(extractor)
                .filter(d -> d >= 0)
                .average()
                .orElse(0);
    }

    private static double parseDoubleSafe(String val) {
        try {
            return val != null ? Double.parseDouble(val) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double round(double val, int places) {
        double scale = Math.pow(10, places);
        return Math.round(val * scale) / scale;
    }
    public static void calculateEloGain(List<MatchStats> matches) {
        Integer nextValidElo = null;

        for (int i = matches.size() - 1; i >= 0; i--) {
            MatchStats current = matches.get(i);
            Integer eloNow = parseElo(current.getElo());

            if (eloNow == null) {
                current.setEloGain(0);
                continue;
            }

            if (nextValidElo != null) {
                int gain = eloNow - nextValidElo;

                if (Math.abs(gain) > 70) {
                    String result = current.getStats().getResult();
                    if ("1".equals(result)) {
                        current.setEloGain(25);
                    } else if ("0".equals(result)) {
                        current.setEloGain(-25);
                    } else {
                        current.setEloGain(0); // fallback if result is weird
                    }
                } else {
                    current.setEloGain(gain);
                }
            } else {
                // No next valid match found, assume elo gain = 0
                current.setEloGain(0);
            }

            nextValidElo = eloNow;
        }
    }


    private static Integer parseElo(String elo) {
        try {
            return elo != null ? Integer.parseInt(elo) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}