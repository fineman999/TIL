package io.chan.queuingsystemforjava.domain.zone.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.zone.dto.ZoneElement;
import io.chan.queuingsystemforjava.domain.zone.service.UserZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances/{performanceId}/zones")
public class UserZoneController {

    private final UserZoneService userZoneService;

    @GetMapping
    public ResponseEntity<ItemResult<ZoneElement>> getZones(
            @PathVariable("performanceId") long performanceId) {
        return ResponseEntity.ok(userZoneService.getZones(performanceId));
    }
}