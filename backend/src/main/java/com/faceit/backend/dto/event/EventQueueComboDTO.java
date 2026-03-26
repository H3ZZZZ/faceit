package com.faceit.backend.dto.event;

import java.util.List;

public class EventQueueComboDTO {
    private List<String> playerNicknames;
    private int lineupSize;
    private int matchesPlayed;
    private int wins;
    private int losses;
    private int winrate;
    private int totalEloGain;

    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }

    public void setPlayerNicknames(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
    }

    public int getLineupSize() {
        return lineupSize;
    }

    public void setLineupSize(int lineupSize) {
        this.lineupSize = lineupSize;
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
}
