package io.chan.queuingsystemforjava.domain.zone.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ZoneRepository {
    private final ZoneJpaRepository zoneJpaRepository;

    public void saveAll(final Collection<Zone> zones) {
        zoneJpaRepository.saveAll(zones);
    }

    public List<Zone> findByPerformance(final Performance performance) {
        return zoneJpaRepository.findByPerformance(performance);
    }

    public Optional<Zone> findById(Long zoneId) {
        return zoneJpaRepository.findById(zoneId);
    }

    public List<Zone> findAll() {
        return zoneJpaRepository.findAll();
    }
}
