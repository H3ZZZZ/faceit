package com.faceit.backend.service.util;

import com.faceit.backend.dto.Didweplay.SharedPlayerStatsDTO;
import com.faceit.backend.dto.Didweplay.SharedPlayerMapStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitPlayerInfo;

import java.util.*;
import java.util.stream.Collectors;

public class SharedStatsAggregator {

    public static SharedPlayerStatsDTO aggregateSharedStats(FaceitPlayerInfo profile, List<MatchStats> matches) {
        // Sort matches by newest first
        matches = new ArrayList<>(matches); // Make it modifiable before sorting
        matches.sort((a, b) -> Long.compare(b.getDate(), a.getDate()));

        int total = matches.size();
        int wins = (int) matches.stream().filter(SharedStatsAggregator::isWin).count();
        int losses = total - wins;
        int eloGain = matches.stream().mapToInt(m -> Optional.ofNullable(m.getEloGain()).orElse(0)).sum();

        double avgKd = average(matches, m -> parseDoubleSafe(m.getStats().getKdRatio()));
        double avgKr = average(matches, m -> parseDoubleSafe(m.getStats().getKrRatio()));
        double avgAdr = average(matches, m -> parseDoubleSafe(m.getStats().getAdr()));
        double avgHs = average(matches, m -> parseDoubleSafe(m.getStats().getHeadshotsPercent()));

        List<String> last5Results = new ArrayList<>();
        int winStreak = 0, lossStreak = 0, maxWinStreak = 0, maxLossStreak = 0;

        for (MatchStats match : matches) {
            boolean win = isWin(match);

            if (win) {
                winStreak++;
                lossStreak = 0;
            } else {
                lossStreak++;
                winStreak = 0;
            }

            maxWinStreak = Math.max(maxWinStreak, winStreak);
            maxLossStreak = Math.max(maxLossStreak, lossStreak);

            if (last5Results.size() < 5) {
                last5Results.add(win ? "W" : "L");
            }
        }
        Collections.reverse(last5Results);

        Map<String, List<MatchStats>> matchesByMap = matches.stream()
                .collect(Collectors.groupingBy(m -> m.getStats().getMap()));

        List<SharedPlayerMapStatsDTO> mapStats = new ArrayList<>();
        for (Map.Entry<String, List<MatchStats>> entry : matchesByMap.entrySet()) {
            String map = entry.getKey();
            List<MatchStats> mapMatches = entry.getValue();

            // Sort each map's matches by newest first
            mapMatches.sort((a, b) -> Long.compare(b.getDate(), a.getDate()));

            int mapWins = (int) mapMatches.stream().filter(SharedStatsAggregator::isWin).count();
            int mapLosses = mapMatches.size() - mapWins;

            int mWinStreak = 0, mLossStreak = 0, mMaxWinStreak = 0, mMaxLossStreak = 0;
            List<String> mLast5Results = new ArrayList<>();
            for (MatchStats match : mapMatches) {
                boolean win = isWin(match);
                if (win) {
                    mWinStreak++;
                    mLossStreak = 0;
                } else {
                    mLossStreak++;
                    mWinStreak = 0;
                }
                mMaxWinStreak = Math.max(mMaxWinStreak, mWinStreak);
                mMaxLossStreak = Math.max(mMaxLossStreak, mLossStreak);
                if (mLast5Results.size() < 5) {
                    mLast5Results.add(win ? "W" : "L");
                }
            }
            Collections.reverse(mLast5Results);
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
            mapDto.setLongestWinStreak(mMaxWinStreak);
            mapDto.setLongestLossStreak(mMaxLossStreak);
            mapDto.setLast5Results(mLast5Results);
            mapStats.add(mapDto);
        }

        SharedPlayerStatsDTO dto = new SharedPlayerStatsDTO();
        dto.setNickname(profile.getNickname());
        dto.setCountry(profile.getCountry());
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
        dto.setLongestWinStreak(maxWinStreak);
        dto.setLongestLossStreak(maxLossStreak);
        dto.setLast5Results(last5Results);

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
                        current.setEloGain(0);
                    }
                } else {
                    current.setEloGain(gain);
                }
            } else {
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
