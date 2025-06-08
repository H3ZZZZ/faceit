package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FaceitMatchStatsResponse {

//    @JsonProperty
//    private List<MatchStats> matches;
//
//    public List<MatchStats> getMatches() {
//        return matches;
//    }

    public static class MatchStats {
        @JsonProperty("i6")
        private String kills;

        @JsonProperty("i7")
        private String assists;

        @JsonProperty("i8")
        private String deaths;

        @JsonProperty("c2")
        private String kdRatio;

        @JsonProperty("c10")
        private String adr;

        @JsonProperty("c4")
        private String headshotsPercent;

        @JsonProperty("i10")
        private String result;

        @JsonProperty("elo")
        private String elo;

        @JsonProperty("nickname")
        private String nickname;

        @JsonProperty("c3")
        private String krRatio;

        @JsonProperty("matchId")
        private String matchId;

        @JsonProperty("date")
        private long date;

        public long getDate() {
            return date;
        }

        public String getMatchId() {
            return matchId;
        }

        public MatchInnerStats getStats() {
            return new MatchInnerStats(kills, assists, deaths, kdRatio, krRatio, adr, headshotsPercent, result);
        }

        public String getElo() {
            return elo;
        }

        public String getNickname() {
            return nickname;
        }
    }

    public static class MatchInnerStats {
        private final String kills;
        private final String assists;
        private final String deaths;
        private final String kdRatio;
        private final String krRatio;
        private final String adr;
        private final String headshotsPercent;
        private final String result;

        public MatchInnerStats(String kills, String assists, String deaths, String kdRatio, String krRatio, String adr, String headshotsPercent, String result) {
            this.kills = kills;
            this.assists = assists;
            this.deaths = deaths;
            this.kdRatio = kdRatio;
            this.krRatio = krRatio;
            this.adr = adr;
            this.headshotsPercent = headshotsPercent;
            this.result = result;
        }

        public String getKills() {
            return kills;
        }

        public String getAssists() {
            return assists;
        }

        public String getDeaths() {
            return deaths;
        }

        public String getKdRatio() {
            return kdRatio;
        }

        public String getAdr() {
            return adr;
        }

        public String getHeadshotsPercent() {
            return headshotsPercent;
        }

        public String getResult() {
            return result;
        }
        public String getKrRatio() {
            return krRatio;
        }
    }
}
