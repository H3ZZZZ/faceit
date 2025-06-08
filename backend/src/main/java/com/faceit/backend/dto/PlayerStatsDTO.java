package com.faceit.backend.dto;

import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerStatsDTO {

    private String playerId;
    private String nickname;
    private String avatar;
    private String country;
    private int skillLevel;
    private int faceitElo;

    private GameSegmentStats last10;
    private GameSegmentStats last30;
    private GameSegmentStats last50;
    private GameSegmentStats last100;

    public static PlayerStatsDTO fromMatches(List<MatchStats> matches, String playerId, List<Integer> eloHistory, List<String> last5Results)
    {
        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.playerId = playerId;

        String nickname = matches.isEmpty() ? "Unknown" : matches.get(0).getNickname();
        dto.nickname = nickname;

        dto.last10 = computeStats(matches.subList(0, Math.min(10, matches.size())), eloHistory, last5Results);
        dto.last30 = computeStats(matches.subList(0, Math.min(30, matches.size())), eloHistory, last5Results);
        dto.last50 = computeStats(matches.subList(0, Math.min(50, matches.size())), eloHistory, last5Results);
        dto.last100 = computeStats(matches, eloHistory, last5Results);

        return dto;
    }

    private static GameSegmentStats computeStats(List<MatchStats> matches, List<Integer> eloHistory, List<String> last5Results) {
        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHsPercent = 0;
        int kills = 0, assists = 0, deaths = 0, wins = 0;
        int winStreak = 0;

        for (int i = 0; i < matches.size(); i++) {
            try {
                MatchInnerStats s = matches.get(i).getStats();
                totalKd += Double.parseDouble(s.getKdRatio());
                totalAdr += Double.parseDouble(s.getAdr());
                totalKr += Double.parseDouble(s.getKrRatio());
                totalHsPercent += Double.parseDouble(s.getHeadshotsPercent());
                kills += Integer.parseInt(s.getKills());
                assists += Integer.parseInt(s.getAssists());
                deaths += Integer.parseInt(s.getDeaths());

                boolean won = "1".equals(s.getResult());
                if (won) wins++;
                if (won && winStreak == i) winStreak++;

            } catch (Exception ignored) {}
        }

        int matchCount = matches.size();
        GameSegmentStats stats = new GameSegmentStats();
        stats.setAverageKd(matchCount > 0 ? round(totalKd / matchCount, 2) : 0);
        stats.setAverageKr(matchCount > 0 ? round(totalKr / matchCount, 2) : 0);
        stats.setAverageAdr(matchCount > 0 ? round(totalAdr / matchCount, 2) : 0);
        stats.setAverageHsPercent(matchCount > 0 ? Math.round(totalHsPercent / matchCount) : 0);
        stats.setWinRate(matchCount > 0 ? Math.round((double) wins / matchCount * 100) : 0);
        stats.setWins(wins);
        stats.setLosses(matchCount - wins);
        stats.setMatchesPlayed(matchCount);
        stats.setKAvg(matchCount > 0 ? (int) Math.round((double) kills / matchCount) : 0);
        stats.setAAvg(matchCount > 0 ? (int) Math.round((double) assists / matchCount) : 0);
        stats.setDAvg(matchCount > 0 ? (int) Math.round((double) deaths / matchCount) : 0);
        stats.setWinStreakCount(winStreak);

        // 🔁 Use shared last5Results (from full 100-match list)
        stats.setLast5Results(new ArrayList<>(last5Results));

        if (eloHistory != null && eloHistory.size() >= 2) {
            stats.setEloChange(eloHistory.get(0) - eloHistory.get(Math.min(eloHistory.size() - 1, matchCount - 1)));
        } else {
            stats.setEloChange(0);
        }

        return stats;
    }


    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public String getPlayerId() { return playerId; }
    public String getNickname() { return nickname; }
    public String getAvatar() { return avatar; }
    public String getCountry() { return country; }
    public int getSkillLevel() { return skillLevel; }
    public int getFaceitElo() { return faceitElo; }
    public GameSegmentStats getLast10() { return last10; }
    public GameSegmentStats getLast30() { return last30; }
    public GameSegmentStats getLast50() { return last50; }
    public GameSegmentStats getLast100() { return last100; }

    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setCountry(String country) { this.country = country; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }

    public static class GameSegmentStats {
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
        private int winStreakCount;
        private List<String> last5Results;

        public double getAverageKd() { return averageKd; }
        public void setAverageKd(double averageKd) { this.averageKd = averageKd; }
        public double getAverageKr() { return averageKr; }
        public void setAverageKr(double averageKr) { this.averageKr = averageKr; }
        public double getAverageAdr() { return averageAdr; }
        public void setAverageAdr(double averageAdr) { this.averageAdr = averageAdr; }
        public double getAverageHsPercent() { return averageHsPercent; }
        public void setAverageHsPercent(double averageHsPercent) { this.averageHsPercent = averageHsPercent; }
        public double getWinRate() { return winRate; }
        public void setWinRate(double winRate) { this.winRate = winRate; }
        public int getMatchesPlayed() { return matchesPlayed; }
        public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }
        public int getWins() { return wins; }
        public void setWins(int wins) { this.wins = wins; }
        public int getLosses() { return losses; }
        public void setLosses(int losses) { this.losses = losses; }
        public int getEloChange() { return eloChange; }
        public void setEloChange(int eloChange) { this.eloChange = eloChange; }
        public int getKAvg() { return kAvg; }
        public void setKAvg(int kAvg) { this.kAvg = kAvg; }
        public int getAAvg() { return aAvg; }
        public void setAAvg(int aAvg) { this.aAvg = aAvg; }
        public int getDAvg() { return dAvg; }
        public void setDAvg(int dAvg) { this.dAvg = dAvg; }
        public int getWinStreakCount() { return winStreakCount; }
        public void setWinStreakCount(int winStreakCount) { this.winStreakCount = winStreakCount; }
        public List<String> getLast5Results() { return last5Results; }
        public void setLast5Results(List<String> last5Results) {
            this.last5Results = last5Results != null ? last5Results : new ArrayList<>();
        }
    }
}
