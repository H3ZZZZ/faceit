package com.faceit.backend.dto.event;

import java.util.List;

public class EventAwardDTO {
    private String title;
    private String winner;
    private List<EventAwardWinnerDTO> winners;
    private String value;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<EventAwardWinnerDTO> getWinners() {
        return winners;
    }

    public void setWinners(List<EventAwardWinnerDTO> winners) {
        this.winners = winners;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
