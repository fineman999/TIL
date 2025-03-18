package io.chan.queuingsystemforjava.domain.performance.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PerformanceRepository {
    private final PerformanceJpaRepository performanceJpaRepository;

    public void save(final Performance performance) {
        performanceJpaRepository.save(performance);
    }

    public List<Performance> findAll() {
        return performanceJpaRepository.findAll();
    }
}
