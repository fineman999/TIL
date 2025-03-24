package io.chan.queuingsystemforjava.domain.ticket.dto.event;

import io.chan.queuingsystemforjava.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatEvent implements Event {
    private final String memberEmail;
    private final Long seatId;
    private final EventType eventType;

    public enum EventType {
        SELECT,
        RELEASE
    }
}