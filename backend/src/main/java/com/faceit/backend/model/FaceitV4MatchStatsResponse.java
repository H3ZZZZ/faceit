package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceitV4MatchStatsResponse {
    @JsonProperty("rounds")
    private List<Round> rounds;

    public List<Round> getRounds() {
        return rounds;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Round {
        @JsonProperty("match_id")
        private String matchId;

        @JsonProperty("round_stats")
        private Map<String, String> roundStats;

        @JsonProperty("teams")
        private List<Team> teams;

        public String getMatchId() {
            return matchId;
        }

        public Map<String, String> getRoundStats() {
            return roundStats;
        }

        public List<Team> getTeams() {
            return teams;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        @JsonProperty("team_id")
        private String teamId;

        @JsonProperty("players")
        private List<Player> players;

        public String getTeamId() {
            return teamId;
        }

        public List<Player> getPlayers() {
            return players;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        @JsonProperty("player_id")
        private String playerId;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("player_stats")
        private Map<String, String> playerStats;

        public String getPlayerId() {
            return playerId;
        }

        public String getNickname() {
            return nickname;
        }

        public Map<String, String> getPlayerStats() {
            return playerStats;
        }
    }
}
