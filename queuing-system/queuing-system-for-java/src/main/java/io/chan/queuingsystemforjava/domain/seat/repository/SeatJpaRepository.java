package io.chan.queuingsystemforjava.domain.seat.repository;

import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByZone(Zone zone);

    @Query(
            """
			SELECT s FROM Seat s
			JOIN FETCH s.zone z
			JOIN FETCH z.performance p
			WHERE p.performanceId = :performanceId
			""")
    List<Seat> findByPerformanceId(@Param("performanceId") long performanceId);
}
