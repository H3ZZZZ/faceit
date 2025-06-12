package com.faceit.backend.service.util;

import com.faceit.backend.dto.MapStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FaceitLifetimeAggregator {

    private static final Logger logger = LoggerFactory.getLogger(FaceitLifetimeAggregator.class);

    public record LifetimeStats(
            int totalEloGain,
            int matchesPlayed,
            int totalWins,
            int totalLosses,
            int eloChange,
            double avgKd,
            double avgKr,
            double avgAdr,
            double avgHsPercent,
            int mostKills,
            String mostKillsMatchId,
            int fewestKills,
            String fewestKillsMatchId,
            int longestWinStreak,
            int longestLossStreak,
            double highestKr,
            String highestKrMatchId,
            double lowestKr,
            String lowestKrMatchId,
            double highestKd,
            String highestKdMatchId,
            double lowestKd,
            String lowestKdMatchId,
            int kavg,
            int aavg,
            int davg,
            List<String> last5Results
    ) {}



    public static LifetimeStats aggregate(List<MatchStats> matches) {
        if (matches == null || matches.isEmpty())
            return new LifetimeStats(
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0,             // avg stats
                    0, null, 0, null,
                    0, 0, 0, null,
                    0, null, 0, null,
                    0, null, 0, 0, 0,
                    new ArrayList<>()
            );


        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHs = 0;
        int validKdCount = 0, validKrCount = 0, validAdrCount = 0, validHsCount = 0;

        int wins = 0, mostKills = Integer.MIN_VALUE, fewestKills = Integer.MAX_VALUE;
        String mostKillsMatchId = "", fewestKillsMatchId = "";
        int totalKills = 0, totalAssists = 0, totalDeaths = 0;

        int eloStart = Integer.parseInt(matches.get(matches.size() - 1).getElo());
        int eloEnd = Integer.parseInt(matches.get(0).getElo());

        int totalEloGain = matches.stream()
                .map(MatchStats::getEloGain)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        int currentWinStreak = 0, currentLossStreak = 0;
        int maxWinStreak = 0, maxLossStreak = 0;

        double highestKr = Double.MIN_VALUE, lowestKr = Double.MAX_VALUE;
        String highestKrMatchId = "", lowestKrMatchId = "";

        double highestKd = Double.MIN_VALUE, lowestKd = Double.MAX_VALUE;
        String highestKdMatchId = "", lowestKdMatchId = "";

        List<String> last5Results = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            MatchStats m = matches.get(i);
            MatchInnerStats s = m.getStats();
            String matchId = m.getMatchId();

            try {
                if (i < 5) {
                    last5Results.add("1".equals(s.getResult()) ? "W" : "L");
                }

                int kills = parseIntSafe(s.getKills());
                int assists = parseIntSafe(s.getAssists());
                int deaths = parseIntSafe(s.getDeaths());
                totalKills += kills;
                totalAssists += assists;
                totalDeaths += deaths;

                if (kills > mostKills) {
                    mostKills = kills;
                    mostKillsMatchId = matchId;
                }
                if (kills < fewestKills) {
                    fewestKills = kills;
                    fewestKillsMatchId = matchId;
                }

                double kd = parseDoubleSafe(s.getKdRatio());
                if (kd >= 0) {
                    totalKd += kd;
                    validKdCount++;
                    if (kd > highestKd) {
                        highestKd = kd;
                        highestKdMatchId = matchId;
                    }
                    if (kd < lowestKd) {
                        lowestKd = kd;
                        lowestKdMatchId = matchId;
                    }
                }

                double kr = parseDoubleSafe(s.getKrRatio());
                if (kr >= 0) {
                    totalKr += kr;
                    validKrCount++;
                    if (kr > highestKr) {
                        highestKr = kr;
                        highestKrMatchId = matchId;
                    }
                    if (kr < lowestKr) {
                        lowestKr = kr;
                        lowestKrMatchId = matchId;
                    }
                }

                double adr = parseDoubleSafe(s.getAdr());
                if (adr >= 0) {
                    totalAdr += adr;
                    validAdrCount++;
                }

                double hs = parseDoubleSafe(s.getHeadshotsPercent());
                if (hs >= 0) {
                    totalHs += hs;
                    validHsCount++;
                }

                boolean win = "1".equals(s.getResult());
                if (win) {
                    wins++;
                    currentWinStreak++;
                    currentLossStreak = 0;
                    maxWinStreak = Math.max(maxWinStreak, currentWinStreak);
                } else {
                    currentLossStreak++;
                    currentWinStreak = 0;
                    maxLossStreak = Math.max(maxLossStreak, currentLossStreak);
                }

            } catch (Exception e) {
                logger.warn("Error parsing match: {}", e.getMessage());
            }
        }

        int totalMatches = matches.size();
        int losses = totalMatches - wins;

        return new LifetimeStats(
                totalEloGain,
                totalMatches,
                wins,
                losses,
                eloEnd - eloStart,
                round(validKdCount > 0 ? totalKd / validKdCount : 0, 2),
                round(validKrCount > 0 ? totalKr / validKrCount : 0, 2),
                round(validAdrCount > 0 ? totalAdr / validAdrCount : 0, 2),
                (int) Math.round(validHsCount > 0 ? totalHs / validHsCount : 0),
                mostKills,
                mostKillsMatchId,
                fewestKills,
                fewestKillsMatchId,
                maxWinStreak,
                maxLossStreak,
                round(highestKr, 2),
                highestKrMatchId,
                round(lowestKr, 2),
                lowestKrMatchId,
                round(highestKd, 2),
                highestKdMatchId,
                round(lowestKd, 2),
                lowestKdMatchId,
                totalKills / totalMatches,
                totalAssists / totalMatches,
                totalDeaths / totalMatches,
                last5Results
        );
    }

    private static double round(double val, int places) {
        double scale = Math.pow(10, places);
        return Math.round(val * scale) / scale;
    }

    public static void calculateEloGain(List<MatchStats> matches) {
        for (int i = 0; i < matches.size(); i++) {
            MatchStats current = matches.get(i);
            Integer eloNow = parseElo(current.getElo());

            if (eloNow == null) {
                current.setEloGain(null);
                continue;
            }

            // Find næste kamp MED en gyldig elo
            Integer eloBefore = null;
            for (int j = i + 1; j < matches.size(); j++) {
                eloBefore = parseElo(matches.get(j).getElo());
                if (eloBefore != null) {
                    break;
                }
            }

            if (eloBefore != null) {
                int eloGain = eloNow - eloBefore;

                // Hvis vi spotter et elo reset
                if (Math.abs(eloGain) > 70) {
                    String result = current.getStats().getResult();
                    if ("1".equals(result)) {
                        current.setEloGain(25);
                    } else if ("0".equals(result)) {
                        current.setEloGain(-25);
                    } else {
                        current.setEloGain(null);
                    }
                } else {
                    current.setEloGain(eloGain);
                }

            } else {
                current.setEloGain(null);
            }
        }
    }

    private static Integer parseElo(String elo) {
        try {
            return elo != null ? Integer.parseInt(elo) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private static double parseDoubleSafe(String val) {
        try {
            return val != null ? Double.parseDouble(val) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int parseIntSafe(String val) {
        try {
            return val != null ? Integer.parseInt(val) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static List<MapStatsDTO> aggregateByMap(List<MatchStats> matches) {
        Map<String, List<MatchStats>> byMap = matches.stream()
                .collect(Collectors.groupingBy(m -> m.getStats().getMap()));

        List<MapStatsDTO> result = new ArrayList<>();

        for (var entry : byMap.entrySet()) {
            String map = entry.getKey();
            List<MatchStats> mapMatches = entry.getValue();

            int wins = 0, totalElo = 0;
            double totalKd = 0, totalKr = 0, totalAdr = 0, totalHs = 0;
            int kdCount = 0, krCount = 0, adrCount = 0, hsCount = 0;
            int totalKills = 0, totalAssists = 0, totalDeaths = 0;

            int currentWinStreak = 0, currentLossStreak = 0;
            int maxWinStreak = 0, maxLossStreak = 0;

            List<String> last5Results = new ArrayList<>();

            for (int i = 0; i < mapMatches.size(); i++) {
                MatchStats m = mapMatches.get(i);
                MatchInnerStats s = m.getStats();

                if (i < 5) {
                    last5Results.add("1".equals(s.getResult()) ? "W" : "L");
                }

                boolean win = "1".equals(s.getResult());
                if (win) {
                    wins++;
                    currentWinStreak++;
                    currentLossStreak = 0;
                    maxWinStreak = Math.max(maxWinStreak, currentWinStreak);
                } else {
                    currentLossStreak++;
                    currentWinStreak = 0;
                    maxLossStreak = Math.max(maxLossStreak, currentLossStreak);
                }

                totalElo += m.getEloGain() != null ? m.getEloGain() : 0;

                try {
                    int kills = parseIntSafe(s.getKills());
                    int assists = parseIntSafe(s.getAssists());
                    int deaths = parseIntSafe(s.getDeaths());
                    totalKills += kills;
                    totalAssists += assists;
                    totalDeaths += deaths;

                    double kd = parseDoubleSafe(s.getKdRatio());
                    if (kd >= 0) {
                        totalKd += kd;
                        kdCount++;
                    }

                    double kr = parseDoubleSafe(s.getKrRatio());
                    if (kr >= 0) {
                        totalKr += kr;
                        krCount++;
                    }

                    double adr = parseDoubleSafe(s.getAdr());
                    if (adr >= 0) {
                        totalAdr += adr;
                        adrCount++;
                    }

                    double hs = parseDoubleSafe(s.getHeadshotsPercent());
                    if (hs >= 0) {
                        totalHs += hs;
                        hsCount++;
                    }

                } catch (NumberFormatException ignored) {}
            }

            int matchCount = mapMatches.size();
            int losses = matchCount - wins;

            MapStatsDTO dto = new MapStatsDTO();
            dto.setMap(map);
            dto.setMatches(matchCount);
            dto.setWins(wins);
            dto.setLosses(losses);
            dto.setTotalEloGain(totalElo);
            dto.setWinrate(matchCount > 0 ? (int) Math.round((double) wins / matchCount * 100) : 0);

            dto.setAvgKd(kdCount > 0 ? round(totalKd / kdCount, 2) : 0);
            dto.setAvgKr(krCount > 0 ? round(totalKr / krCount, 2) : 0);
            dto.setAvgAdr(adrCount > 0 ? round(totalAdr / adrCount, 2) : 0);
            dto.setAvgHsPercent(hsCount > 0 ? (int) Math.round(totalHs / hsCount) : 0);

            dto.setKavg(matchCount > 0 ? totalKills / matchCount : 0);
            dto.setAavg(matchCount > 0 ? totalAssists / matchCount : 0);
            dto.setDavg(matchCount > 0 ? totalDeaths / matchCount : 0);

            dto.setLongestWinStreak(maxWinStreak);
            dto.setLongestLossStreak(maxLossStreak);
            dto.setLast5Results(last5Results);

            result.add(dto);
        }

        return result;
    }

}