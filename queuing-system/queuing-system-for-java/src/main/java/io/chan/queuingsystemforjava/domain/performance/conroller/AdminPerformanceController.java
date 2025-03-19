package io.chan.queuingsystemforjava.domain.performance.conroller;

import io.chan.queuingsystemforjava.domain.performance.dto.CreatePerformanceRequest;
import io.chan.queuingsystemforjava.domain.performance.service.AdminPerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
public class AdminPerformanceController {

    private final AdminPerformanceService adminPerformanceService;

    @PostMapping
    public ResponseEntity<Void> createPerformance(
            @RequestBody @Valid CreatePerformanceRequest performanceCreationRequest) {
        adminPerformanceService.createPerformance(performanceCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}