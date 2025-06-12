package com.faceit.backend.dto;

import java.util.List;

public class MapStatsDTO {
    private String map;
    private int matches;
    private int wins;
    private int losses;
    private int winrate;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int totalEloGain;

    // 🆕 New fields:
    private int kavg;
    private int aavg;
    private int davg;
    private int longestWinStreak;
    private int longestLossStreak;
    private List<String> last5Results;


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
    public int getWinrate() {
        return winrate;
    }
    public void setWinrate(int winrate) {
        this.winrate = winrate;
    }
    public int getKavg() {
        return kavg;
    }
    public void setKavg(int kavg) {
        this.kavg = kavg;
    }
    public int getAavg() {
        return aavg;
    }
    public void setAavg(int aavg) {
        this.aavg = aavg;
    }
    public int getDavg() {
        return davg;
    }
    public void setDavg(int davg) {
        this.davg = davg;
    }
    public int getLongestWinStreak() {
        return longestWinStreak;
    }
    public void setLongestWinStreak(int longestWinStreak) {
        this.longestWinStreak = longestWinStreak;
    }
    public int getLongestLossStreak() {
        return longestLossStreak;
    }
    public void setLongestLossStreak(int longestLossStreak) {
        this.longestLossStreak = longestLossStreak;
    }
    public List<String> getLast5Results() {
        return last5Results;
    }
    public void setLast5Results(List<String> last5Results) {
        this.last5Results = last5Results;
    }

}
