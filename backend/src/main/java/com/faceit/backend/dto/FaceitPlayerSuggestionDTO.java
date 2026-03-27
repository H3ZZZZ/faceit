package com.faceit.backend.dto;

public class FaceitPlayerSuggestionDTO {
    private String playerId;
    private String nickname;
    private String avatar;
    private String country;
    private int skillLevel;
    private int faceitElo;

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getSkillLevel() { return skillLevel; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
    public int getFaceitElo() { return faceitElo; }
    public void setFaceitElo(int faceitElo) { this.faceitElo = faceitElo; }
}
