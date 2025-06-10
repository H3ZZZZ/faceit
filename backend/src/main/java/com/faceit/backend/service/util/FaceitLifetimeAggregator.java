package com.faceit.backend.service.util;

import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;

import java.util.List;

public class FaceitLifetimeAggregator {

    public record LifetimeStats(
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
            String lowestKdMatchId
    ) {}

    public static LifetimeStats aggregate(List<MatchStats> matches) {
        if (matches == null || matches.isEmpty()) return new LifetimeStats(0, 0, 0, 0, 0, 0, 0, 0, 0, null, 0, null, 0, 0, 0, null, 0, null, 0, null, 0, null);

        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHs = 0;
        int wins = 0, kills, mostKills = Integer.MIN_VALUE, fewestKills = Integer.MAX_VALUE;
        String mostKillsMatchId = "", fewestKillsMatchId = "";
        int eloStart = Integer.parseInt(matches.get(matches.size() - 1).getElo());
        int eloEnd = Integer.parseInt(matches.get(0).getElo());

        int currentWinStreak = 0, currentLossStreak = 0;
        int maxWinStreak = 0, maxLossStreak = 0;

        double highestKr = Double.MIN_VALUE, lowestKr = Double.MAX_VALUE;
        String highestKrMatchId = "", lowestKrMatchId = "";

        double highestKd = Double.MIN_VALUE, lowestKd = Double.MAX_VALUE;
        String highestKdMatchId = "", lowestKdMatchId = "";

        for (MatchStats m : matches) {
            try {
                MatchInnerStats s = m.getStats();

                double kd = Double.parseDouble(s.getKdRatio());
                double kr = Double.parseDouble(s.getKrRatio());
                double adr = Double.parseDouble(s.getAdr());
                double hs = Double.parseDouble(s.getHeadshotsPercent());
                int killCount = Integer.parseInt(s.getKills());

                totalKd += kd;
                totalKr += kr;
                totalAdr += adr;
                totalHs += hs;

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

                if (killCount > mostKills) {
                    mostKills = killCount;
                    mostKillsMatchId = m.getMatchId();
                }

                if (killCount < fewestKills) {
                    fewestKills = killCount;
                    fewestKillsMatchId = m.getMatchId();
                }

                if (kr > highestKr) {
                    highestKr = kr;
                    highestKrMatchId = m.getMatchId();
                }

                if (kr < lowestKr) {
                    lowestKr = kr;
                    lowestKrMatchId = m.getMatchId();
                }

                if (kd > highestKd) {
                    highestKd = kd;
                    highestKdMatchId = m.getMatchId();
                }

                if (kd < lowestKd) {
                    lowestKd = kd;
                    lowestKdMatchId = m.getMatchId();
                }

            } catch (Exception ignored) {}
        }

        int totalMatches = matches.size();
        int losses = totalMatches - wins;

        return new LifetimeStats(
                totalMatches,
                wins,
                losses,
                eloEnd - eloStart,
                round(totalKd / totalMatches, 2),
                round(totalKr / totalMatches, 2),
                round(totalAdr / totalMatches, 2),
                round(totalHs / totalMatches, 2),
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
                lowestKdMatchId
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
                        current.setEloGain(null); // fallback hvis result er tomt
                    }
                } else {
                    current.setEloGain(eloGain);
                }

            } else {
                current.setEloGain(null); // kunne ikke finde gyldig "before elo"
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



}
