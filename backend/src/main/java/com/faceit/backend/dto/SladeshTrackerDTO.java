package com.faceit.backend.dto;

public record SladeshTrackerDTO(
        String nickname,
        int elo,
        int level,
        Long lastTimeLevel10,
        String matchIdLevel10,
        Long lastTimeDroppedBelow10,
        String matchIdDroppedBelow10
) {}
