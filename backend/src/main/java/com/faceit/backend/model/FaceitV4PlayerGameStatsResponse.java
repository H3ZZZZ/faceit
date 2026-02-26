package com.faceit.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceitV4PlayerGameStatsResponse {
    private List<Item> items;
    private Integer start;
    private Integer end;

    public List<Item> getItems() {
        return items;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private Map<String, Object> stats;

        public Map<String, Object> getStats() {
            return stats;
        }
    }
}
