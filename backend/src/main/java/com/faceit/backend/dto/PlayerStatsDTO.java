package com.faceit.backend.dto;

public class PlayerStatsDTO {
    private int gamesCount;
    private int wins;
    private int losses;
    private int winRate;
    private int eloDiff;
    private int avgKills;
    private int avgAssists;
    private int avgDeaths;
    private double avgKD;
    private double avgKR;
    private double avgADR;
    private int avgHS;

    public PlayerStatsDTO(int gamesCount, int wins, int losses, int winRate, int eloDiff,
                          int avgKills, int avgAssists, int avgDeaths, double avgKD, double avgKR,
                          double avgADR, int avgHS) {
        this.gamesCount = gamesCount;
        this.wins = wins;
        this.losses = losses;
        this.winRate = winRate;
        this.eloDiff = eloDiff;
        this.avgKills = avgKills;
        this.avgAssists = avgAssists;
        this.avgDeaths = avgDeaths;
        this.avgKD = avgKD;
        this.avgKR = avgKR;
        this.avgADR = avgADR;
        this.avgHS = avgHS;
    }

    public PlayerStatsDTO() {}

    // Getters and setters
    public int getGamesCount() { return gamesCount; }
    public void setGamesCount(int gamesCount) { this.gamesCount = gamesCount; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getWinRate() { return winRate; }
    public void setWinRate(int winRate) { this.winRate = winRate; }

    public int getEloDiff() { return eloDiff; }
    public void setEloDiff(int eloDiff) { this.eloDiff = eloDiff; }

    public int getAvgKills() { return avgKills; }
    public void setAvgKills(int avgKills) { this.avgKills = avgKills; }

    public int getAvgAssists() { return avgAssists; }
    public void setAvgAssists(int avgAssists) { this.avgAssists = avgAssists; }

    public int getAvgDeaths() { return avgDeaths; }
    public void setAvgDeaths(int avgDeaths) { this.avgDeaths = avgDeaths; }

    public double getAvgKD() { return avgKD; }
    public void setAvgKD(double avgKD) { this.avgKD = avgKD; }

    public double getAvgKR() { return avgKR; }
    public void setAvgKR(double avgKR) { this.avgKR = avgKR; }

    public double getAvgADR() { return avgADR; }
    public void setAvgADR(double avgADR) { this.avgADR = avgADR; }

    public int getAvgHS() { return avgHS; }
    public void setAvgHS(int avgHS) { this.avgHS = avgHS; }
}
