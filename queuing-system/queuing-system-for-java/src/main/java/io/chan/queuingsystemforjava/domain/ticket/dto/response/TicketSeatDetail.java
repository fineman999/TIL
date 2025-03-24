package io.chan.queuingsystemforjava.domain.ticket.dto.response;

import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatGradeElement;

public record TicketSeatDetail(
        Long seatId,
        String seatCode,
        SeatGradeElement grade
) {
    public static TicketSeatDetail of(Seat seat) {
        return new TicketSeatDetail(
                seat.getSeatId(),
                seat.getSeatCode(),
                SeatGradeElement.of(seat.getSeatGrade())
        );
    }
}
