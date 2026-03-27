package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceitMatchDetailsResponse {
    @JsonProperty("match_id")
    private String matchId;

    @JsonProperty("started_at")
    private long startedAt;

    @JsonProperty("finished_at")
    private long finishedAt;

    public String getMatchId() {
        return matchId;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getFinishedAt() {
        return finishedAt;
    }
}
