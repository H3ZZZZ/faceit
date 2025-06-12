package com.faceit.backend.service.util;

import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FaceitLifetimeAggregator {

    private static final Logger logger = LoggerFactory.getLogger(FaceitLifetimeAggregator.class);

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
        if (matches == null || matches.isEmpty())
            return new LifetimeStats(0, 0, 0, 0, 0, 0, 0, 0, 0, null, 0, null, 0, 0, 0, null, 0, null, 0, null, 0, null);

        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHs = 0;
        int validKdCount = 0, validKrCount = 0, validAdrCount = 0, validHsCount = 0;

        int wins = 0, mostKills = Integer.MIN_VALUE, fewestKills = Integer.MAX_VALUE;
        String mostKillsMatchId = "", fewestKillsMatchId = "";
        int eloStart = Integer.parseInt(matches.get(matches.size() - 1).getElo());
        int eloEnd = Integer.parseInt(matches.get(0).getElo());

        int currentWinStreak = 0, currentLossStreak = 0;
        int maxWinStreak = 0, maxLossStreak = 0;

        double highestKr = Double.MIN_VALUE, lowestKr = Double.MAX_VALUE;
        String highestKrMatchId = "", lowestKrMatchId = "";

        double highestKd = Double.MIN_VALUE, lowestKd = Double.MAX_VALUE;
        String highestKdMatchId = "", lowestKdMatchId = "";

        for (int i = 0; i < matches.size(); i++) {
            MatchStats m = matches.get(i);
            MatchInnerStats s = m.getStats();
            String matchId = m.getMatchId();

            try {
                String kdRaw = s.getKdRatio();
                if (kdRaw != null) {
                    double kd = Double.parseDouble(kdRaw.trim());
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

                String krRaw = s.getKrRatio();
                if (krRaw != null) {
                    double kr = Double.parseDouble(krRaw.trim());
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

                String adrRaw = s.getAdr();
                if (adrRaw != null) {
                    double adr = Double.parseDouble(adrRaw.trim());
                    totalAdr += adr;
                    validAdrCount++;
                }

                String hsRaw = s.getHeadshotsPercent();
                if (hsRaw != null) {
                    double hs = Double.parseDouble(hsRaw.trim());
                    totalHs += hs;
                    validHsCount++;
                }

                String killsRaw = s.getKills();
                int killCount = (killsRaw != null) ? Integer.parseInt(killsRaw.trim()) : 0;
                if (killCount > mostKills) {
                    mostKills = killCount;
                    mostKillsMatchId = matchId;
                }
                if (killCount < fewestKills) {
                    fewestKills = killCount;
                    fewestKillsMatchId = matchId;
                }

                String result = s.getResult();
                boolean win = "1".equals(result);
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

            } catch (NumberFormatException e) {
                logger.warn("Skipped one or more stats for match index {} [matchId={}] due to parse error: {}", i, matchId, e.getMessage());
                logger.debug("Raw data: kd='{}', kr='{}', adr='{}', hs='{}', kills='{}'", s.getKdRatio(), s.getKrRatio(), s.getAdr(), s.getHeadshotsPercent(), s.getKills());
            } catch (Exception e) {
                logger.warn("Unexpected error in match index {} [matchId={}]: {}", i, matchId, e.toString());
            }
        }

        int totalMatches = matches.size();
        int losses = totalMatches - wins;

        return new LifetimeStats(
                totalMatches,
                wins,
                losses,
                eloEnd - eloStart,
                round(validKdCount > 0 ? totalKd / validKdCount : 0, 2),
                round(validKrCount > 0 ? totalKr / validKrCount : 0, 2),
                round(validAdrCount > 0 ? totalAdr / validAdrCount : 0, 2),
                round(validHsCount > 0 ? totalHs / validHsCount : 0, 2),
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
}