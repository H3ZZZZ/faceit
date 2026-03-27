package com.faceit.backend.controller;

import com.faceit.backend.dto.FaceitPlayerSuggestionDTO;
import com.faceit.backend.service.FaceitPlayerSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class FaceitPlayerController {
    private final FaceitPlayerSearchService faceitPlayerSearchService;

    public FaceitPlayerController(FaceitPlayerSearchService faceitPlayerSearchService) {
        this.faceitPlayerSearchService = faceitPlayerSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FaceitPlayerSuggestionDTO>> searchPlayers(@RequestParam String query) {
        return ResponseEntity.ok(faceitPlayerSearchService.searchPlayers(query));
    }
}
