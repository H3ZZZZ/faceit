package com.faceit.backend.controller;

import com.faceit.backend.dto.event.GogoLanEventDTO;
import com.faceit.backend.service.GogoLanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final GogoLanService gogoLanService;

    public EventController(GogoLanService gogoLanService) {
        this.gogoLanService = gogoLanService;
    }

    @GetMapping("/gogo-lan")
    public ResponseEntity<GogoLanEventDTO> getGogoLanEvent(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end) {
        return ResponseEntity.ok(gogoLanService.getEvent(start, end));
    }
}
