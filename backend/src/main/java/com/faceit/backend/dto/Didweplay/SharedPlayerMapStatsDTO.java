package com.faceit.backend.dto.Didweplay;

public class SharedPlayerMapStatsDTO {
    private String map;
    private int matches;
    private int wins;
    private int losses;
    private double winrate;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int totalEloGain;

    // Getters and setters
    public String getMap() {
        return map;
    }
    public void setMap(String map) {
        this.map = map;
    }
    public int getMatches() {
        return matches;
    }
    public void setMatches(int matches) {
        this.matches = matches;
    }
    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public double getWinrate() {
        return winrate;
    }
    public void setWinrate(double winrate) {
        this.winrate = winrate;
    }
    public double getAvgKd() {
        return avgKd;
    }
    public void setAvgKd(double avgKd) {
        this.avgKd = avgKd;
    }
    public double getAvgKr() {
        return avgKr;
    }
    public void setAvgKr(double avgKr) {
        this.avgKr = avgKr;
    }
    public double getAvgAdr() {
        return avgAdr;
    }
    public void setAvgAdr(double avgAdr) {
        this.avgAdr = avgAdr;
    }
    public double getAvgHsPercent() {
        return avgHsPercent;
    }
    public void setAvgHsPercent(double avgHsPercent) {
        this.avgHsPercent = avgHsPercent;
    }
    public int getTotalEloGain() {
        return totalEloGain;
    }
    public void setTotalEloGain(int totalEloGain) {
        this.totalEloGain = totalEloGain;
    }

}
