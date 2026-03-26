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
    private int entryCount;
    private int entryWins;
    private double entrySuccessRate;
    private double entryAttemptRate;
    private int clutchWins;
    private int clutch1v1Wins;
    private int clutch1v2Wins;
    private int clutchKills;
    private int mvps;
    private double mvpsPerGame;
    private int sniperKills;
    private double sniperKillsPerGame;
    private int pistolKills;
    private double pistolKillsPerGame;
    private int doubleKills;
    private int tripleKills;
    private int quadroKills;
    private int pentaKills;
    private int multiKillRounds;
    private double multiKillRoundsPerGame;
    private int flashSuccesses;
    private int enemiesFlashed;
    private double enemiesFlashedPerRound;
    private double flashSuccessRate;
    private int utilityDamage;
    private int utilitySuccesses;
    private int utilityCount;
    private int roundsPlayed;
    private double utilityDamagePerRound;
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

    public int getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

    public int getEntryWins() {
        return entryWins;
    }

    public void setEntryWins(int entryWins) {
        this.entryWins = entryWins;
    }

    public double getEntrySuccessRate() {
        return entrySuccessRate;
    }

    public void setEntrySuccessRate(double entrySuccessRate) {
        this.entrySuccessRate = entrySuccessRate;
    }

    public double getEntryAttemptRate() {
        return entryAttemptRate;
    }

    public void setEntryAttemptRate(double entryAttemptRate) {
        this.entryAttemptRate = entryAttemptRate;
    }

    public int getClutchWins() {
        return clutchWins;
    }

    public void setClutchWins(int clutchWins) {
        this.clutchWins = clutchWins;
    }

    public int getClutch1v1Wins() {
        return clutch1v1Wins;
    }

    public void setClutch1v1Wins(int clutch1v1Wins) {
        this.clutch1v1Wins = clutch1v1Wins;
    }

    public int getClutch1v2Wins() {
        return clutch1v2Wins;
    }

    public void setClutch1v2Wins(int clutch1v2Wins) {
        this.clutch1v2Wins = clutch1v2Wins;
    }

    public int getClutchKills() {
        return clutchKills;
    }

    public void setClutchKills(int clutchKills) {
        this.clutchKills = clutchKills;
    }

    public int getMvps() {
        return mvps;
    }

    public void setMvps(int mvps) {
        this.mvps = mvps;
    }

    public double getMvpsPerGame() {
        return mvpsPerGame;
    }

    public void setMvpsPerGame(double mvpsPerGame) {
        this.mvpsPerGame = mvpsPerGame;
    }

    public int getSniperKills() {
        return sniperKills;
    }

    public void setSniperKills(int sniperKills) {
        this.sniperKills = sniperKills;
    }

    public double getSniperKillsPerGame() {
        return sniperKillsPerGame;
    }

    public void setSniperKillsPerGame(double sniperKillsPerGame) {
        this.sniperKillsPerGame = sniperKillsPerGame;
    }

    public int getPistolKills() {
        return pistolKills;
    }

    public void setPistolKills(int pistolKills) {
        this.pistolKills = pistolKills;
    }

    public double getPistolKillsPerGame() {
        return pistolKillsPerGame;
    }

    public void setPistolKillsPerGame(double pistolKillsPerGame) {
        this.pistolKillsPerGame = pistolKillsPerGame;
    }

    public int getDoubleKills() {
        return doubleKills;
    }

    public void setDoubleKills(int doubleKills) {
        this.doubleKills = doubleKills;
    }

    public int getTripleKills() {
        return tripleKills;
    }

    public void setTripleKills(int tripleKills) {
        this.tripleKills = tripleKills;
    }

    public int getQuadroKills() {
        return quadroKills;
    }

    public void setQuadroKills(int quadroKills) {
        this.quadroKills = quadroKills;
    }

    public int getPentaKills() {
        return pentaKills;
    }

    public void setPentaKills(int pentaKills) {
        this.pentaKills = pentaKills;
    }

    public int getMultiKillRounds() {
        return multiKillRounds;
    }

    public void setMultiKillRounds(int multiKillRounds) {
        this.multiKillRounds = multiKillRounds;
    }

    public double getMultiKillRoundsPerGame() {
        return multiKillRoundsPerGame;
    }

    public void setMultiKillRoundsPerGame(double multiKillRoundsPerGame) {
        this.multiKillRoundsPerGame = multiKillRoundsPerGame;
    }

    public int getFlashSuccesses() {
        return flashSuccesses;
    }

    public void setFlashSuccesses(int flashSuccesses) {
        this.flashSuccesses = flashSuccesses;
    }

    public int getEnemiesFlashed() {
        return enemiesFlashed;
    }

    public void setEnemiesFlashed(int enemiesFlashed) {
        this.enemiesFlashed = enemiesFlashed;
    }

    public double getEnemiesFlashedPerRound() {
        return enemiesFlashedPerRound;
    }

    public void setEnemiesFlashedPerRound(double enemiesFlashedPerRound) {
        this.enemiesFlashedPerRound = enemiesFlashedPerRound;
    }

    public double getFlashSuccessRate() {
        return flashSuccessRate;
    }

    public void setFlashSuccessRate(double flashSuccessRate) {
        this.flashSuccessRate = flashSuccessRate;
    }

    public int getUtilityDamage() {
        return utilityDamage;
    }

    public void setUtilityDamage(int utilityDamage) {
        this.utilityDamage = utilityDamage;
    }

    public int getUtilitySuccesses() {
        return utilitySuccesses;
    }

    public void setUtilitySuccesses(int utilitySuccesses) {
        this.utilitySuccesses = utilitySuccesses;
    }

    public int getUtilityCount() {
        return utilityCount;
    }

    public void setUtilityCount(int utilityCount) {
        this.utilityCount = utilityCount;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public double getUtilityDamagePerRound() {
        return utilityDamagePerRound;
    }

    public void setUtilityDamagePerRound(double utilityDamagePerRound) {
        this.utilityDamagePerRound = utilityDamagePerRound;
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
