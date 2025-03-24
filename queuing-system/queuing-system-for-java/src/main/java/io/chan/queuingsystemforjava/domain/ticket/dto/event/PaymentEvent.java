package io.chan.queuingsystemforjava.domain.ticket.dto.event;

import io.chan.queuingsystemforjava.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentEvent implements Event {
    private final String email;
}