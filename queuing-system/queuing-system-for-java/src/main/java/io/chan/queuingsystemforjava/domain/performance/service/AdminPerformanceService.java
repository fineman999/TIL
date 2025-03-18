package io.chan.queuingsystemforjava.domain.performance.service;

import io.chan.queuingsystemforjava.domain.performance.dto.CreatePerformanceRequest;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPerformanceService {
    private final PerformanceRepository performanceRepository;

    public void createPerformance(final CreatePerformanceRequest request) {
        performanceRepository.save(request.toEntity());
    }
}
