package io.chan.queuingsystemforjava.domain.waitingsystem.controller;

import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WaitingController {

    private final WaitingSystem waitingSystem;

    @GetMapping("/performances/{performanceId}/wait")
    public ResponseEntity<Map<String, Long>> getRemainingCount(
            @LoginMember String email, @PathVariable("performanceId") Long performanceId) {
        long remainingCount = waitingSystem.pollRemainingCountAndTriggerEvents(email, performanceId);
        return ResponseEntity.ok(Map.of("remainingCount", remainingCount));
    }

    @DeleteMapping("/performances/{performanceId}/wait")
    public ResponseEntity<Void> removeMemberInfo(
            @LoginMember String email, @PathVariable("performanceId") Long performanceId) {
        waitingSystem.pullOutRunningMember(email, performanceId);
        return ResponseEntity.noContent().build();
    }
}