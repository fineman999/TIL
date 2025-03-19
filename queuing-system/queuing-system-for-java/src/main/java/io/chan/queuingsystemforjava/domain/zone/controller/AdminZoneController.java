package io.chan.queuingsystemforjava.domain.zone.controller;

import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneRequest;
import io.chan.queuingsystemforjava.domain.zone.service.AdminZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/performances/{performanceId}/zones")
@RequiredArgsConstructor
public class AdminZoneController {

    private final AdminZoneService adminZoneService;

    @PostMapping
    public ResponseEntity<Void> createZones(
            @PathVariable("performanceId") long performanceId,
            @RequestBody @Valid CreateZoneRequest zoneCreationRequest) {
        adminZoneService.createZones(performanceId, zoneCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}