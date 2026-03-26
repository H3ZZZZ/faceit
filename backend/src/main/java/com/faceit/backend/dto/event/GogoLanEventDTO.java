package com.faceit.backend.dto.event;

import java.time.LocalDate;
import java.util.List;

public class GogoLanEventDTO {
    private String key;
    private String name;
    private String zoneId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int playerCount;
    private int totalUniqueMatches;
    private List<EventPlayerStatsDTO> players;
    private List<EventAwardDTO> awards;
    private List<EventQueueComboDTO> queueCombos;
    private List<EventMapSummaryDTO> mapLeaderboard;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getTotalUniqueMatches() {
        return totalUniqueMatches;
    }

    public void setTotalUniqueMatches(int totalUniqueMatches) {
        this.totalUniqueMatches = totalUniqueMatches;
    }

    public List<EventPlayerStatsDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<EventPlayerStatsDTO> players) {
        this.players = players;
    }

    public List<EventAwardDTO> getAwards() {
        return awards;
    }

    public void setAwards(List<EventAwardDTO> awards) {
        this.awards = awards;
    }

    public List<EventQueueComboDTO> getQueueCombos() {
        return queueCombos;
    }

    public void setQueueCombos(List<EventQueueComboDTO> queueCombos) {
        this.queueCombos = queueCombos;
    }

    public List<EventMapSummaryDTO> getMapLeaderboard() {
        return mapLeaderboard;
    }

    public void setMapLeaderboard(List<EventMapSummaryDTO> mapLeaderboard) {
        this.mapLeaderboard = mapLeaderboard;
    }
}
