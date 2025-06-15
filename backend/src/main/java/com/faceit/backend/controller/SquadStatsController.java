package com.faceit.backend.controller;

import com.faceit.backend.dto.Didweplay.SharedPlayerStatsDTO;
import com.faceit.backend.service.SquadStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/squad-stats")
public class SquadStatsController {

    private final SquadStatsService squadStatsService;

    public SquadStatsController(SquadStatsService squadStatsService) {
        this.squadStatsService = squadStatsService;
    }

    @PostMapping
    public ResponseEntity<List<SharedPlayerStatsDTO>> getSquadStats(@RequestBody List<String> nicknames) {
        List<SharedPlayerStatsDTO> result = squadStatsService.getSharedStats(nicknames);
        return ResponseEntity.ok(result);
    }
}

