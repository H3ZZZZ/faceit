package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceitPlayerInfo {

    private String avatar;
    private String country;

    @JsonProperty("games")
    private Games games;

    public String getAvatar() { return avatar; }
    public String getCountry() { return country; }
    public int getSkillLevel() {
        return games != null && games.cs2 != null ? games.cs2.skillLevel : 0;
    }
    public int getFaceitElo() {
        return games != null && games.cs2 != null ? games.cs2.faceitElo : 0;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Games {
        @JsonProperty("cs2")
        private Cs2 cs2;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cs2 {
        @JsonProperty("skill_level")
        private int skillLevel;

        @JsonProperty("faceit_elo")
        private int faceitElo;
    }
}