package com.faceit.backend.dto;

public class LifetimeStatsDTO {
    private int matchesPlayed;
    private int totalWins;
    private int totalLosses;
    private int eloChange;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int mostKills;
    private String mostKillsMatchId;
    private int fewestKills;
    private String fewestKillsMatchId;
    private int longestWinStreak;
    private int longestLossStreak;
    private double highestKr;
    private String highestKrMatchId;
    private double lowestKr;
    private String lowestKrMatchId;
    private double highestKd;
    private String highestKdMatchId;
    private double lowestKd;
    private String lowestKdMatchId;



    // Getters and setters
    public double getHighestKd() { return highestKd; }
    public void setHighestKd(double highestKd) { this.highestKd = highestKd; }
    public String getHighestKdMatchId() { return highestKdMatchId; }
    public void setHighestKdMatchId(String highestKdMatchId) { this.highestKdMatchId = highestKdMatchId; }
    public double getLowestKd() { return lowestKd; }
    public void setLowestKd(double lowestKd) { this.lowestKd = lowestKd; }
    public String getLowestKdMatchId() { return lowestKdMatchId; }
    public void setLowestKdMatchId(String lowestKdMatchId) { this.lowestKdMatchId = lowestKdMatchId; }

    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public int getTotalWins() { return totalWins; }
    public void setTotalWins(int totalWins) { this.totalWins = totalWins; }

    public int getTotalLosses() { return totalLosses; }
    public void setTotalLosses(int totalLosses) { this.totalLosses = totalLosses; }

    public int getEloChange() { return eloChange; }
    public void setEloChange(int eloChange) { this.eloChange = eloChange; }

    public double getAvgKd() { return avgKd; }
    public void setAvgKd(double avgKd) { this.avgKd = avgKd; }

    public double getAvgKr() { return avgKr; }
    public void setAvgKr(double avgKr) { this.avgKr = avgKr; }

    public double getAvgAdr() { return avgAdr; }
    public void setAvgAdr(double avgAdr) { this.avgAdr = avgAdr; }

    public double getAvgHsPercent() { return avgHsPercent; }
    public void setAvgHsPercent(double avgHsPercent) { this.avgHsPercent = avgHsPercent; }

    public int getMostKills() { return mostKills; }
    public void setMostKills(int mostKills) { this.mostKills = mostKills; }

    public String getMostKillsMatchId() { return mostKillsMatchId; }
    public void setMostKillsMatchId(String mostKillsMatchId) { this.mostKillsMatchId = mostKillsMatchId; }

    public int getFewestKills() { return fewestKills; }
    public void setFewestKills(int fewestKills) { this.fewestKills = fewestKills; }

    public String getFewestKillsMatchId() { return fewestKillsMatchId; }
    public void setFewestKillsMatchId(String fewestKillsMatchId) { this.fewestKillsMatchId = fewestKillsMatchId; }

    public int getLongestWinStreak() { return longestWinStreak; }
    public void setLongestWinStreak(int longestWinStreak) { this.longestWinStreak = longestWinStreak; }

    public int getLongestLossStreak() { return longestLossStreak; }
    public void setLongestLossStreak(int longestLossStreak) { this.longestLossStreak = longestLossStreak; }

    public double getHighestKr() { return highestKr; }
    public void setHighestKr(double highestKr) { this.highestKr = highestKr; }

    public String getHighestKrMatchId() { return highestKrMatchId; }
    public void setHighestKrMatchId(String highestKrMatchId) { this.highestKrMatchId = highestKrMatchId; }

    public double getLowestKr() { return lowestKr; }
    public void setLowestKr(double lowestKr) { this.lowestKr = lowestKr; }

    public String getLowestKrMatchId() { return lowestKrMatchId; }
    public void setLowestKrMatchId(String lowestKrMatchId) { this.lowestKrMatchId = lowestKrMatchId; }

}
