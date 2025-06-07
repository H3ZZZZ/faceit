package com.faceit.backend.dto;

public class PlayerStatsResponse {
    private String playerId;
    private String nickname;
    private PlayerStatsDTO stats10;
    private PlayerStatsDTO stats30;
    private PlayerStatsDTO stats50;
    private PlayerStatsDTO stats99;

    public PlayerStatsResponse(String playerId, String nickname,
                               PlayerStatsDTO stats10, PlayerStatsDTO stats30,
                               PlayerStatsDTO stats50, PlayerStatsDTO stats99) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.stats10 = stats10;
        this.stats30 = stats30;
        this.stats50 = stats50;
        this.stats99 = stats99;
    }

    // Getters and setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public PlayerStatsDTO getStats10() { return stats10; }
    public void setStats10(PlayerStatsDTO stats10) { this.stats10 = stats10; }

    public PlayerStatsDTO getStats30() { return stats30; }
    public void setStats30(PlayerStatsDTO stats30) { this.stats30 = stats30; }

    public PlayerStatsDTO getStats50() { return stats50; }
    public void setStats50(PlayerStatsDTO stats50) { this.stats50 = stats50; }

    public PlayerStatsDTO getStats99() { return stats99; }
    public void setStats99(PlayerStatsDTO stats99) { this.stats99 = stats99; }
}
