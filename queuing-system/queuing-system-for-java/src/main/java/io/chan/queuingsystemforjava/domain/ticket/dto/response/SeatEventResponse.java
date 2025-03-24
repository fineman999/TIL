package io.chan.queuingsystemforjava.domain.ticket.dto.response;

public record SeatEventResponse (
    Long seatId,
    String status
){
}
