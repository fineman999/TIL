package io.chan.queuingsystemforjava.domain.performance.conroller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.performance.dto.PerformanceElement;
import io.chan.queuingsystemforjava.domain.performance.service.UserPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
public class UserPerformanceController {

    private final UserPerformanceService userPerformanceService;

    @GetMapping()
    public ResponseEntity<ItemResult<PerformanceElement>> getPerformances() {
        return ResponseEntity.ok(userPerformanceService.getPerformances());
    }

    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceElement> getPerformance(
            @PathVariable("performanceId") long performanceId) {
        return ResponseEntity.ok(userPerformanceService.getPerformance(performanceId));
    }
}