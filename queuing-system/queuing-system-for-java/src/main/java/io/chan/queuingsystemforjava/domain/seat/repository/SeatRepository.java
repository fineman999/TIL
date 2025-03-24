package io.chan.queuingsystemforjava.domain.seat.repository;

import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepository {
    private final SeatJpaRepository seatJpaRepository;
    public List<Seat> findByZone(Zone zone) {
        return seatJpaRepository.findByZone(zone);
    }

    public List<Seat> findByPerformanceId(long performanceId) {
        return seatJpaRepository.findByPerformanceId(performanceId);
    }

    public void saveAll(List<Seat> seats) {
        seatJpaRepository.saveAll(seats);
    }

    public List<Seat> findAll() {
        return seatJpaRepository.findAll();
    }
}
