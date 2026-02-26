package com.faceit.backend.controller;

import com.faceit.backend.dto.PlayerStatsDTO;
import com.faceit.backend.dto.SladeshTrackerDTO;
import com.faceit.backend.dto.SladeshSimpleDTO;
import com.faceit.backend.service.FaceitStatsService;
import com.faceit.backend.service.FaceitStatsV4Service;
import com.faceit.backend.service.SladeshTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class FaceitStatsController {

    private final FaceitStatsService statsService;
    private final FaceitStatsV4Service statsV4Service;
    private final SladeshTrackerService sladeshTrackerService;

    public FaceitStatsController(FaceitStatsService statsService,
                                 FaceitStatsV4Service statsV4Service,
                                 SladeshTrackerService sladeshTrackerService) {
        this.statsService = statsService;
        this.statsV4Service = statsV4Service;
        this.sladeshTrackerService = sladeshTrackerService;
    }

    // Single player: /api/stats/{playerId}
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerStatsDTO> getStatsForPlayer(@PathVariable String playerId) {
        PlayerStatsDTO stats = statsService.getLast30Stats(playerId);
        return ResponseEntity.ok(stats);
    }

    // v4-only variant: /api/stats/v4/{playerId}
    @GetMapping("/v4/{playerId}")
    public ResponseEntity<PlayerStatsDTO> getStatsForPlayerV4(@PathVariable String playerId) {
        return ResponseEntity.ok(statsV4Service.getStatsForPlayer(playerId));
    }

    @GetMapping("/sladesh-tracker")
    public ResponseEntity<SladeshTrackerDTO> getSladeshTracker() {
        return ResponseEntity.ok(sladeshTrackerService.getSladeshTrackerData());
    }

    @GetMapping("/sladesh/simple")
    public ResponseEntity<SladeshSimpleDTO> getSladeshSimple() {
        return ResponseEntity.ok(sladeshTrackerService.getSladeshSimpleInfo());
    }



    // Multiple players: /api/stats?ids=ID1,ID2,...
    @GetMapping
    public ResponseEntity<List<PlayerStatsDTO>> getStatsForPlayers(@RequestParam List<String> ids) {
        List<PlayerStatsDTO> stats = statsService.getStatsForPlayers(ids);
        return ResponseEntity.ok(stats);
    }

    // Multiple players via v4: /api/stats/v4?ids=ID1,ID2,...
    @GetMapping("/v4")
    public ResponseEntity<List<PlayerStatsDTO>> getStatsForPlayersV4(@RequestParam List<String> ids) {
        return ResponseEntity.ok(statsV4Service.getStatsForPlayers(ids));
    }
}
