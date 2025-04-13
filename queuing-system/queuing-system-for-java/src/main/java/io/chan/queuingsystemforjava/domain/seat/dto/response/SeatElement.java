package io.chan.queuingsystemforjava.domain.seat.dto.response;

import io.chan.queuingsystemforjava.domain.seat.Seat;

import java.math.BigDecimal;

public record SeatElement(
        Long seatId,
        String seatCode,
        boolean seatAvailable,
        BigDecimal price,
        String gradeName
) {
    public static SeatElement fromSeat(Seat seat) {
        boolean isSeatAvailable = seat.isSelectable();
        return new SeatElement(seat.getSeatId(), seat.getSeatCode(), isSeatAvailable,
                seat.getSeatGrade().getPrice(), seat.getSeatGrade().getGradeName());
    }
}
