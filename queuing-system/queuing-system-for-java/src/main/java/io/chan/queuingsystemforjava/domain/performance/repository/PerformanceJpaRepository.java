package io.chan.queuingsystemforjava.domain.performance.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceJpaRepository extends JpaRepository<Performance, Long> {}
