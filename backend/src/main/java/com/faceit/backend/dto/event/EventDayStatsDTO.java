package com.faceit.backend.dto.event;

import java.time.LocalDate;
import java.util.List;

public class EventDayStatsDTO {
    private LocalDate date;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int winrate;
    private int totalEloGain;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int kavg;
    private int aavg;
    private int davg;
    private double sessionHours;
    private List<String> lastResults;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public int getTotalEloGain() {
        return totalEloGain;
    }

    public void setTotalEloGain(int totalEloGain) {
        this.totalEloGain = totalEloGain;
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

    public double getSessionHours() {
        return sessionHours;
    }

    public void setSessionHours(double sessionHours) {
        this.sessionHours = sessionHours;
    }

    public List<String> getLastResults() {
        return lastResults;
    }

    public void setLastResults(List<String> lastResults) {
        this.lastResults = lastResults;
    }
}
