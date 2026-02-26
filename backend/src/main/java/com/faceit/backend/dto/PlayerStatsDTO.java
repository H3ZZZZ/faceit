package com.faceit.backend.dto;

import com.faceit.backend.model.FaceitMatchStatsResponse.MatchStats;
import com.faceit.backend.model.FaceitMatchStatsResponse.MatchInnerStats;
import com.faceit.backend.model.FaceitV4PlayerGameStatsResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlayerStatsDTO {

    private String playerId;
    private String nickname;
    private String avatar;
    private String country;
    private int skillLevel;
    private int faceitElo;
    private long lastActive;

    private GameSegmentStats last10;
    private GameSegmentStats last30;
    private GameSegmentStats last50;
    private GameSegmentStats last100;

    public static PlayerStatsDTO fromMatches(List<MatchStats> matches, String playerId, List<Integer> eloHistory, List<String> last5Results)
    {
        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.playerId = playerId;
        dto.lastActive = matches.isEmpty() ? 0 : matches.get(0).getCreatedAt();

        String nickname = matches.isEmpty() ? "Unknown" : matches.get(0).getNickname();
        dto.nickname = nickname;

        dto.last10 = computeStats(matches.subList(0, Math.min(10, matches.size())), eloHistory.subList(0, Math.min(10, eloHistory.size())), last5Results);
        dto.last30 = computeStats(matches.subList(0, Math.min(30, matches.size())), eloHistory.subList(0, Math.min(30, eloHistory.size())), last5Results);
        dto.last50 = computeStats(matches.subList(0, Math.min(50, matches.size())), eloHistory.subList(0, Math.min(50, eloHistory.size())), last5Results);
        dto.last100 = computeStats(matches, eloHistory, last5Results);

        return dto;
    }

    public static PlayerStatsDTO fromV4Matches(List<FaceitV4PlayerGameStatsResponse.Item> items, String playerId) {
        List<FaceitV4PlayerGameStatsResponse.Item> safeItems = items != null ? items : List.of();

        PlayerStatsDTO dto = new PlayerStatsDTO();
        dto.playerId = playerId;
        dto.lastActive = safeItems.isEmpty() ? 0 : parseV4Timestamp(safeItems.get(0).getStats());
        dto.nickname = safeItems.isEmpty() ? "Unknown" : v4Stat(safeItems.get(0).getStats(), "Nickname");

        List<String> last5Results = new ArrayList<>();
        for (int i = 0; i < Math.min(5, safeItems.size()); i++) {
            String result = v4Stat(safeItems.get(i).getStats(), "Result");
            last5Results.add("1".equals(result) ? "W" : "L");
        }
        Collections.reverse(last5Results);

        dto.last10 = computeStatsFromV4(safeItems.subList(0, Math.min(10, safeItems.size())), last5Results);
        dto.last30 = computeStatsFromV4(safeItems.subList(0, Math.min(30, safeItems.size())), last5Results);
        dto.last50 = computeStatsFromV4(safeItems.subList(0, Math.min(50, safeItems.size())), last5Results);
        dto.last100 = computeStatsFromV4(safeItems.subList(0, Math.min(100, safeItems.size())), last5Results);

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
        stats.setNewKD(deaths > 0 ? round((double) kills / deaths, 2) : 0);
        stats.setWinStreakCount(winStreak);

        // 🔁 Use shared last5Results (from full 100-match list)
        stats.setLast5Results(new ArrayList<>(last5Results));

        Integer startElo = findFirstValidElo(matches);
        Integer endElo = findLastValidElo(matches);
        if (startElo != null && endElo != null) {
            stats.setEloChange(startElo - endElo);
        } else {
            stats.setEloChange(0);
        }


        return stats;
    }

    private static GameSegmentStats computeStatsFromV4(List<FaceitV4PlayerGameStatsResponse.Item> items, List<String> last5Results) {
        double totalKd = 0, totalKr = 0, totalAdr = 0, totalHsPercent = 0;
        int kills = 0, assists = 0, deaths = 0, wins = 0;
        int winStreak = 0;

        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> statsMap = items.get(i).getStats();
            totalKd += parseDouble(v4Stat(statsMap, "K/D Ratio"));
            totalAdr += parseDouble(v4Stat(statsMap, "ADR"));
            totalKr += parseDouble(v4Stat(statsMap, "K/R Ratio"));
            totalHsPercent += parseDouble(v4Stat(statsMap, "Headshots %"));
            kills += parseInt(v4Stat(statsMap, "Kills"));
            assists += parseInt(v4Stat(statsMap, "Assists"));
            deaths += parseInt(v4Stat(statsMap, "Deaths"));

            boolean won = "1".equals(v4Stat(statsMap, "Result"));
            if (won) wins++;
            if (won && winStreak == i) winStreak++;
        }

        int matchCount = items.size();
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
        stats.setNewKD(deaths > 0 ? round((double) kills / deaths, 2) : 0);
        stats.setWinStreakCount(winStreak);
        stats.setLast5Results(new ArrayList<>(last5Results));

        // v4 payload does not reliably expose elo/elo_delta in the documented stats response.
        stats.setEloChange(0);

        return stats;
    }


    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    private static Integer findFirstValidElo(List<MatchStats> matches) {
        for (MatchStats match : matches) {
            try {
                return Integer.parseInt(match.getElo());
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static Integer findLastValidElo(List<MatchStats> matches) {
        for (int i = matches.size() - 1; i >= 0; i--) {
            try {
                return Integer.parseInt(matches.get(i).getElo());
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static String v4Stat(Map<String, Object> stats, String key) {
        if (stats == null) return "";
        Object value = stats.get(key);
        return value == null ? "" : String.valueOf(value);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static long parseV4Timestamp(Map<String, Object> stats) {
        String finished = v4Stat(stats, "Match Finished At");
        try {
            return Long.parseLong(finished);
        } catch (Exception ignored) {
        }

        String createdAt = v4Stat(stats, "Created At");
        try {
            return Instant.parse(createdAt).toEpochMilli();
        } catch (Exception ignored) {
            return 0;
        }
    }


    public String getPlayerId() { return playerId; }
    public String getNickname() { return nickname; }
    public String getAvatar() { return avatar; }
    public String getCountry() { return country; }
    public int getSkillLevel() { return skillLevel; }
    public int getFaceitElo() { return faceitElo; }
    public long getLastActive() { return lastActive; }
    public GameSegmentStats getLast10() { return last10; }
    public GameSegmentStats getLast30() { return last30; }
    public GameSegmentStats getLast50() { return last50; }
    public GameSegmentStats getLast100() { return last100; }

    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setCountry(String country) { this.country = country; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }
    public void setLastActive(long lastActive) { this.lastActive = lastActive; }

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
        private double newKD;
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
        public double getNewKD() { return newKD; }
        public void setNewKD(double newKD) { this.newKD = newKD; }
        public int getWinStreakCount() { return winStreakCount; }
        public void setWinStreakCount(int winStreakCount) { this.winStreakCount = winStreakCount; }
        public List<String> getLast5Results() { return last5Results; }
        public void setLast5Results(List<String> last5Results) {
            this.last5Results = last5Results != null ? last5Results : new ArrayList<>();
        }
    }
}


