package io.chan.queuingsystemforjava.domain.performance.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public Optional<Performance> findById(final long performanceId) {
        return performanceJpaRepository.findById(performanceId);
    }
}
