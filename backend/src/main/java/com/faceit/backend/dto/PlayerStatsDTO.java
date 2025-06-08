package com.faceit.backend.dto;

import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsDTO {

    private String playerId;
    private String nickname;
    private double averageKd;
    private double averageKr;
    private double averageAdr;
    private double averageHsPercent;
    private double winRate;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int eloChange;
    private int kAvg;
    private int aAvg;
    private int dAvg;
    private String avatar;
    private String country;
    private int skillLevel;
    private int faceitElo;
    private int winStreakCount;
    private List<String> last5Results; ;

    public static PlayerStatsDTO fromMatches(List<MatchStats> matches, String playerId, List<Integer> eloHistory) {
        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHsPercent = 0;
        int kills = 0, assists = 0, deaths = 0, wins = 0;
        int validMatches = 0;

        String nickname = null;

        for (MatchStats match : matches) {
            try {
                MatchInnerStats s = match.getStats();
                totalKd += Double.parseDouble(s.getKdRatio());
                totalAdr += Double.parseDouble(s.getAdr());
                totalKr += Double.parseDouble(s.getKrRatio());
                totalHsPercent += Double.parseDouble(s.getHeadshotsPercent());
                kills += Integer.parseInt(s.getKills());
                assists += Integer.parseInt(s.getAssists());
                deaths += Integer.parseInt(s.getDeaths());

                if ("1".equals(s.getResult())) {
                    wins++;
                }
                if (nickname == null) nickname = match.getNickname();
                validMatches++;
            } catch (Exception ignored) {}
        }

        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.playerId = playerId;
        dto.nickname = nickname != null ? nickname : "Unknown";
        dto.averageKd = validMatches > 0 ? round(totalKd / validMatches, 2) : 0;
        dto.averageKr = validMatches > 0 ? round(totalKr / validMatches, 2) : 0;
        dto.averageAdr = validMatches > 0 ? round(totalAdr / validMatches, 2) : 0;
        dto.averageHsPercent = validMatches > 0 ? Math.round(totalHsPercent / validMatches) : 0;
        dto.winRate = validMatches > 0 ? Math.round((double) wins / validMatches * 100) : 0;
        dto.wins = wins;
        dto.losses = validMatches - wins;
        dto.matchesPlayed = validMatches;
        dto.kAvg = validMatches > 0 ? (int) Math.round((double) kills / validMatches) : 0;
        dto.aAvg = validMatches > 0 ? (int) Math.round((double) assists / validMatches) : 0;
        dto.dAvg = validMatches > 0 ? (int) Math.round((double) deaths / validMatches) : 0;

        if (eloHistory != null && eloHistory.size() >= 2) {
            dto.eloChange = eloHistory.get(0) - eloHistory.get(eloHistory.size() - 1);
        } else {
            dto.eloChange = 0;
        }

        // 🟢 Add streak and last 5 result fields
        List<String> last5 = new ArrayList<>();
        int winStreak = 0;

// Determine win streak (count wins from most recent match until first loss)
        for (int i = 0; i < matches.size(); i++) {
            try {
                boolean won = "1".equals(matches.get(i).getStats().getResult());
                if (won) {
                    winStreak++;
                } else {
                    break;
                }
            } catch (Exception ignored) {}
        }

// Collect the last 5 results (most recent games first)
        int total = matches.size();
        for (int i = total - 1; i >= 0 && last5.size() < 5; i--) {
            try {
                boolean won = "1".equals(matches.get(i).getStats().getResult());
                last5.add(won ? "W" : "L");
            } catch (Exception ignored) {}
        }

// Optional: reverse to show most recent result first
// E.g., W, L, L, L, W (most recent → oldest)
        java.util.Collections.reverse(last5);

        dto.setLast5Results(last5);
        dto.setWinStreakCount(winStreak);


        return dto;
    }


    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    // Getters
    public String getPlayerId() { return playerId; }
    public String getNickname() { return nickname; }
    public double getAverageKd() { return averageKd; }
    public double getAverageKr() { return averageKr; }
    public double getAverageAdr() { return averageAdr; }
    public double getAverageHsPercent() { return averageHsPercent; }
    public double getWinRate() { return winRate; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getEloChange() { return eloChange; }
    public int getKAvg() { return kAvg; }
    public int getAAvg() { return aAvg; }
    public int getDAvg() { return dAvg; }
    public String getAvatar() { return avatar; }
    public String getCountry() { return country; }
    public int getSkillLevel() { return skillLevel; }
    public int getFaceitElo() { return faceitElo; }
    public int getWinStreakCount() { return winStreakCount; }
    public List<String> getLast5Results() { return last5Results; }

    // Setters
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setCountry(String country) { this.country = country; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }
    public void setWinStreakCount(int winStreakCount) { this.winStreakCount = winStreakCount; }
    public void setLast5Results(List<String> last5Results) {
        this.last5Results = last5Results != null ? last5Results : new ArrayList<>(); }

}
