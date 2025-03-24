package io.chan.queuingsystemforjava.domain.seat.controller;

import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatGradeCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.service.AdminSeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdminSeatController {
    private final AdminSeatService adminSeatService;

    @PostMapping("/performances/{performanceId}/zones/{zoneId}/seats")
    public ResponseEntity<Void> createSeats(
            @PathVariable("performanceId") long performanceId,
            @PathVariable("zoneId") long zoneId,
            @RequestBody @Valid SeatCreationRequest seatCreationRequest) {
        adminSeatService.createSeats(performanceId, zoneId, seatCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/performances/{performanceId}/grades")
    public ResponseEntity<Void> createSeatGrades(
            @PathVariable("performanceId") long performanceId,
            @RequestBody @Valid SeatGradeCreationRequest seatGradeCreationRequest) {
        adminSeatService.createSeatGrades(performanceId, seatGradeCreationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}