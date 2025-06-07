package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class FaceitEloTimelineResponse {

    @JsonProperty("timeline")
    private List<TimelineEntry> timeline;

    public List<Integer> getElo() {
        List<Integer> result = new ArrayList<>();
        if (timeline != null) {
            for (TimelineEntry entry : timeline) {
                result.add(entry.getElo());
            }
        }
        return result;
    }

    public static class TimelineEntry {

        @JsonProperty("elo")
        private int elo;

        public int getElo() {
            return elo;
        }
    }
}
