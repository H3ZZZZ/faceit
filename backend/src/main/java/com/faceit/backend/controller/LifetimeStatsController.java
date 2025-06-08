package com.faceit.backend.controller;

import com.faceit.backend.dto.LifetimeStatsDTO;
import com.faceit.backend.service.FaceitLifetimeStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lifetime-stats")
public class LifetimeStatsController {

    private final FaceitLifetimeStatsService lifetimeStatsService;

    public LifetimeStatsController(FaceitLifetimeStatsService lifetimeStatsService) {
        this.lifetimeStatsService = lifetimeStatsService;
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<LifetimeStatsDTO> getLifetimeStats(@PathVariable String playerId) {
        LifetimeStatsDTO stats = lifetimeStatsService.getLifetimeStats(playerId);
        return ResponseEntity.ok(stats);
    }
}
