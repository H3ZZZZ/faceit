package com.faceit.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Matchroom {

    private String matchId;
    private String nickname;
    private String playerId;

    private int i6;   // kills
    private int i7;   // assists
    private int i8;   // deaths
    private int i10;  // win = 1

    private double c2;  // KD
    private double c3;  // KR
    private double c10; // ADR
    private int c4;     // HS

    private Integer elo;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getI6() {
        return i6;
    }

    public void setI6(int i6) {
        this.i6 = i6;
    }

    public int getI7() {
        return i7;
    }

    public void setI7(int i7) {
        this.i7 = i7;
    }

    public int getI8() {
        return i8;
    }

    public void setI8(int i8) {
        this.i8 = i8;
    }

    public int getI10() {
        return i10;
    }

    public void setI10(int i10) {
        this.i10 = i10;
    }

    public double getC2() {
        return c2;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    public double getC3() {
        return c3;
    }

    public void setC3(double c3) {
        this.c3 = c3;
    }

    public double getC10() {
        return c10;
    }

    public void setC10(double c10) {
        this.c10 = c10;
    }

    public int getC4() {
        return c4;
    }

    public void setC4(int c4) {
        this.c4 = c4;
    }

    public Integer getElo() {
        return elo;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }
}
