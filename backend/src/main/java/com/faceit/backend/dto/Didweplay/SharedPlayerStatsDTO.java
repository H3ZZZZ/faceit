package com.faceit.backend.dto.Didweplay;

import java.util.List;

public class SharedPlayerStatsDTO {
    private String nickname;
    private String avatar;
    private int skillLevel;
    private int faceitElo;

    private int totalMatches;
    private int wins;
    private int losses;
    private double winrate;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int totalEloGain;

    private List<SharedPlayerMapStatsDTO> mapStats;

    // Getters and setters
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public int getSkillLevel() { return skillLevel; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }

    public int getFaceitElo() { return faceitElo; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }

    public int getTotalMatches() { return totalMatches; }
    public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public double getWinrate() { return winrate; }
    public void setWinrate(double winrate) { this.winrate = winrate; }

    public double getAvgKd() { return avgKd; }
    public void setAvgKd(double avgKd) { this.avgKd = avgKd; }

    public double getAvgKr() { return avgKr; }
    public void setAvgKr(double avgKr) { this.avgKr = avgKr; }

    public double getAvgAdr() { return avgAdr; }
    public void setAvgAdr(double avgAdr) { this.avgAdr = avgAdr; }

    public double getAvgHsPercent() { return avgHsPercent; }
    public void setAvgHsPercent(double avgHsPercent) { this.avgHsPercent = avgHsPercent; }

    public int getTotalEloGain() { return totalEloGain; }
    public void setTotalEloGain(int totalEloGain) { this.totalEloGain = totalEloGain; }


    public List<SharedPlayerMapStatsDTO> getMapStats() { return mapStats; }
    public void setMapStats(List<SharedPlayerMapStatsDTO> mapStats) { this.mapStats = mapStats; }
}
