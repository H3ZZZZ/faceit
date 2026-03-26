package com.faceit.backend.dto.event;

public class EventMapSummaryDTO {
    private String map;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int winrate;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int totalEloGain;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
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

    public int getWinrate() {
        return winrate;
    }

    public void setWinrate(int winrate) {
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
