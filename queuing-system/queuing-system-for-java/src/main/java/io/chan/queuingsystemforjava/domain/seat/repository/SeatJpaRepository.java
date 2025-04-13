package io.chan.queuingsystemforjava.domain.seat.repository;

import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
  @Query(
      """
			SELECT s FROM Seat s
			JOIN FETCH s.zone z
			JOIN FETCH s.seatGrade sg
			WHERE s.zone = :zone
			""")
  List<Seat> findByZone(Zone zone);

  @Query(
      """
			SELECT s FROM Seat s
			JOIN FETCH s.zone z
			JOIN FETCH s.seatGrade sg
			JOIN FETCH z.performance p
			WHERE p.performanceId = :performanceId
			""")
  List<Seat> findByPerformanceId(@Param("performanceId") long performanceId);

  @Query("SELECT s FROM Seat as s WHERE s.seatId = :seatId")
  @Lock(LockModeType.OPTIMISTIC)
  Optional<Seat> findByIdWithOptimistic(@Param("seatId") Long seatId);

  @Query("SELECT s FROM Seat as s WHERE s.seatId = :seatId")
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Seat> findByIdWithPessimistic(@Param("seatId") Long seatId);
}
