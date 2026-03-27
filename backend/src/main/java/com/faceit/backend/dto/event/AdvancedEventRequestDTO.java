package com.faceit.backend.dto.event;

import java.time.LocalDate;
import java.util.List;

public class AdvancedEventRequestDTO {
    private List<String> playerIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;

    public List<String> getPlayerIds() { return playerIds; }
    public void setPlayerIds(List<String> playerIds) { this.playerIds = playerIds; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
