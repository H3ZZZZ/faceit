package com.faceit.backend.dto;

import java.util.List;

public class LifetimeStatsDTO {
    private int matchesPlayed;
    private int totalWins;
    private int totalLosses;
    private int winrate;
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
    private int totalEloGain;
    private int highestElo;
    private int lowestElo;


    // 🆕 New fields:
    private int kavg;
    private int aavg;
    private int davg;
    private List<String> last5Results;

    // 🔁 Existing fields (like avatar, country, etc.)
    private String avatar;
    private String country;
    private int faceitElo;
    private int skillLevel;
    private String game_player_id;
    private String nickname;

    // 🔁 Map stats
    private List<MapStatsDTO> mapStats;


    public List<MapStatsDTO> getMapStats() { return mapStats; }
    public void setMapStats(List<MapStatsDTO> mapStats) { this.mapStats = mapStats; }




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

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getFaceitElo() { return faceitElo; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }

    public int getSkillLevel() { return skillLevel; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }

    public String getGame_player_id() { return game_player_id; }
    public void setGame_player_id(String game_player_id) { this.game_player_id = game_player_id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getTotalEloGain() { return totalEloGain; }
    public void setTotalEloGain(int totalEloGain) { this.totalEloGain = totalEloGain;}

    public int getWinrate() {
        return winrate;
    }
    public void setWinrate(int winrate) {
        this.winrate = winrate;
    }
    public int getKavg() { return kavg; }
    public void setKavg(int kavg) { this.kavg = kavg; }

    public int getAavg() { return aavg; }
    public void setAavg(int aavg) { this.aavg = aavg; }

    public int getDavg() { return davg; }
    public void setDavg(int davg) { this.davg = davg; }

    public List<String> getLast5Results() { return last5Results; }
    public void setLast5Results(List<String> last5Results) { this.last5Results = last5Results; }
    public int getHighestElo() { return highestElo; }
    public void setHighestElo(int highestElo) { this.highestElo = highestElo; }
    public int getLowestElo() { return lowestElo; }
    public void setLowestElo(int lowestElo) { this.lowestElo = lowestElo; }



}
