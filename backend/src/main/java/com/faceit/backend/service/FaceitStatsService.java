package com.faceit.backend.service;

import com.faceit.backend.dto.PlayerStatsDTO;

import java.util.List;

public interface FaceitStatsService {
    PlayerStatsDTO getLast30Stats(String playerId);
    List<PlayerStatsDTO> getStatsForPlayers(List<String> playerIds);
}
