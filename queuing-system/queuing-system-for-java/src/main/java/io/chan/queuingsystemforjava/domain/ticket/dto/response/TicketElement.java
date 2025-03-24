package io.chan.queuingsystemforjava.domain.ticket.dto.response;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.dto.PerformanceElement;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;

import java.util.UUID;

public record TicketElement (
        UUID serialNumber,
        PerformanceElement performance,
        TicketSeatDetail seat
){
    public static TicketElement of(Ticket ticket) {
        Seat seat = ticket.getSeat();
        Performance performance = seat.getZone().getPerformance();
        return new TicketElement(
                ticket.getTicketSerialNumber(),
                PerformanceElement.of(performance),
                TicketSeatDetail.of(seat)
        );
    }
}
