package io.chan.queuingsystemforjava.domain.ticket.strategy;

import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PessimisticLockSeatStrategy implements LockSeatStrategy {
    private final SeatRepository seatRepository;

    @Override
    public Optional<Seat> getSeatWithLock(Long seatId) {
        return seatRepository.findByIdWithPessimistic(seatId);
    }
}