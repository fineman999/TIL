package io.chan.queuingsystemforjava.domain.zone.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoneJpaRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByPerformance(Performance performance);
}
