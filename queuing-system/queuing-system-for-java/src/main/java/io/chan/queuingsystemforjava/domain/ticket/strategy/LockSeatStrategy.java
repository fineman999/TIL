package io.chan.queuingsystemforjava.domain.ticket.strategy;

import io.chan.queuingsystemforjava.domain.seat.Seat;

import java.util.Optional;

public interface LockSeatStrategy {
    Optional<Seat> getSeatWithLock(Long seatId);
}