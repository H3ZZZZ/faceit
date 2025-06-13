package com.faceit.backend.controller;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.dto.SladeshTrackerDTO;
import com.faceit.backend.service.FaceitStatsService;
import com.faceit.backend.service.SladeshTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class FaceitStatsController {

    private final FaceitStatsService statsService;
    private final SladeshTrackerService sladeshTrackerService;

    public FaceitStatsController(FaceitStatsService statsService, SladeshTrackerService sladeshTrackerService) {
        this.statsService = statsService;
        this.sladeshTrackerService = sladeshTrackerService;
    }

    // Single player: /api/stats/{playerId}
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerStatsDTO> getStatsForPlayer(@PathVariable String playerId) {
        PlayerStatsDTO stats = statsService.getLast30Stats(playerId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/sladesh-tracker")
    public ResponseEntity<SladeshTrackerDTO> getSladeshTracker() {
        return ResponseEntity.ok(sladeshTrackerService.getSladeshTrackerData());
    }


    // Multiple players: /api/stats?ids=ID1,ID2,...
    @GetMapping
    public ResponseEntity<List<PlayerStatsDTO>> getStatsForPlayers(@RequestParam List<String> ids) {
        List<PlayerStatsDTO> stats = statsService.getStatsForPlayers(ids);
        return ResponseEntity.ok(stats);
    }
}
