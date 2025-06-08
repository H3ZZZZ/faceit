package com.faceit.backend.service;

import com.faceit.backend.dto.LifetimeStatsDTO;
import com.faceit.backend.model.FaceitMatchStatsResponse;

import java.util.List;

public interface FaceitLifetimeStatsService {
    List<FaceitMatchStatsResponse.MatchStats> fetchAllCs2Matches(String playerId);

    LifetimeStatsDTO getLifetimeStats(String playerId);
}