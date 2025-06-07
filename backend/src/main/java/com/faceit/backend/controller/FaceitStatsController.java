package com.faceit.backend.controller;

import com.faceit.backend.dto.PlayerStatsResponse;
import com.faceit.backend.service.FaceitStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class FaceitStatsController {

    private final FaceitStatsService statsService;

    public FaceitStatsController(FaceitStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/{playerId}")
    public PlayerStatsResponse getStats(@PathVariable String playerId) throws Exception {
        return statsService.getPlayerStats(playerId);
    }
}
