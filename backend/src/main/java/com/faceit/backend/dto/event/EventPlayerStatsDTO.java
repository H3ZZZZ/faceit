package com.faceit.backend.dto.event;

import com.faceit.backend.dto.MapStatsDTO;

import java.time.Instant;
import java.util.List;

public class EventPlayerStatsDTO {
    private String playerId;
    private String nickname;
    private String avatar;
    private String country;
    private int skillLevel;
    private int faceitElo;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int winrate;
    private int totalEloGain;
    private int eloChange;
    private double avgKd;
    private double avgKr;
    private double avgAdr;
    private double avgHsPercent;
    private int kavg;
    private int aavg;
    private int davg;
    private int longestWinStreak;
    private int longestLossStreak;
    private List<String> last5Results;
    private double totalSessionHours;
    private Instant lastMatchAt;
    private String mostPlayedMap;
    private String bestMap;
    private String worstMap;
    private EventDayStatsDTO bestDay;
    private EventDayStatsDTO worstDay;
    private List<EventDayStatsDTO> dailyStats;
    private List<MapStatsDTO> mapStats;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getFaceitElo() {
        return faceitElo;
    }

    public void setFaceitElo(int faceitElo) {
        this.faceitElo = faceitElo;
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

    public int getEloChange() {
        return eloChange;
    }

    public void setEloChange(int eloChange) {
        this.eloChange = eloChange;
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

    public double getTotalSessionHours() {
        return totalSessionHours;
    }

    public void setTotalSessionHours(double totalSessionHours) {
        this.totalSessionHours = totalSessionHours;
    }

    public Instant getLastMatchAt() {
        return lastMatchAt;
    }

    public void setLastMatchAt(Instant lastMatchAt) {
        this.lastMatchAt = lastMatchAt;
    }

    public String getMostPlayedMap() {
        return mostPlayedMap;
    }

    public void setMostPlayedMap(String mostPlayedMap) {
        this.mostPlayedMap = mostPlayedMap;
    }

    public String getBestMap() {
        return bestMap;
    }

    public void setBestMap(String bestMap) {
        this.bestMap = bestMap;
    }

    public String getWorstMap() {
        return worstMap;
    }

    public void setWorstMap(String worstMap) {
        this.worstMap = worstMap;
    }

    public EventDayStatsDTO getBestDay() {
        return bestDay;
    }

    public void setBestDay(EventDayStatsDTO bestDay) {
        this.bestDay = bestDay;
    }

    public EventDayStatsDTO getWorstDay() {
        return worstDay;
    }

    public void setWorstDay(EventDayStatsDTO worstDay) {
        this.worstDay = worstDay;
    }

    public List<EventDayStatsDTO> getDailyStats() {
        return dailyStats;
    }

    public void setDailyStats(List<EventDayStatsDTO> dailyStats) {
        this.dailyStats = dailyStats;
    }

    public List<MapStatsDTO> getMapStats() {
        return mapStats;
    }

    public void setMapStats(List<MapStatsDTO> mapStats) {
        this.mapStats = mapStats;
    }
}
