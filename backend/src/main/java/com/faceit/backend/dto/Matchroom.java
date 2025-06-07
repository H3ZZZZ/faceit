package com.faceit.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Matchroom {

    @JsonProperty("matchId")
    private String matchId;

    @JsonProperty("playerId")
    private String playerId;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("i10")
    private int i10; // win = 1, loss = 0

    @JsonProperty("i6")
    private int i6; // kills

    @JsonProperty("i7")
    private int i7; // assists

    @JsonProperty("i8")
    private int i8; // deaths

    @JsonProperty("c2")
    private double c2; // K/D

    @JsonProperty("c3")
    private double c3; // K/R

    @JsonProperty("c4")
    private int c4; // HS%

    @JsonProperty("c10")
    private double c10; // ADR

    @JsonProperty("elo")
    private Integer elo;

    // Getters and Setters
    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getI10() { return i10; }
    public void setI10(int i10) { this.i10 = i10; }

    public int getI6() { return i6; }
    public void setI6(int i6) { this.i6 = i6; }

    public int getI7() { return i7; }
    public void setI7(int i7) { this.i7 = i7; }

    public int getI8() { return i8; }
    public void setI8(int i8) { this.i8 = i8; }

    public double getC2() { return c2; }
    public void setC2(double c2) { this.c2 = c2; }

    public double getC3() { return c3; }
    public void setC3(double c3) { this.c3 = c3; }

    public int getC4() { return c4; }
    public void setC4(int c4) { this.c4 = c4; }

    public double getC10() { return c10; }
    public void setC10(double c10) { this.c10 = c10; }

    public Integer getElo() { return elo; }
    public void setElo(Integer elo) { this.elo = elo; }
}
