package io.chan.queuingsystemforjava.domain.seat.dto.response;

import io.chan.queuingsystemforjava.domain.seat.Seat;

public record SeatElement(
        Long seatId,
        String seatCode,
        boolean seatAvailable
) {
    public static SeatElement fromSeat(Seat seat) {
        boolean isSeatAvailable = seat.isSelectable();
        return new SeatElement(seat.getSeatId(), seat.getSeatCode(), isSeatAvailable);
    }
}
