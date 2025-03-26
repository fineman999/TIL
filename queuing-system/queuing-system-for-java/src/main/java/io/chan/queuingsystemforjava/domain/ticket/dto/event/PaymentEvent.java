package io.chan.queuingsystemforjava.domain.ticket.dto.event;

import io.chan.queuingsystemforjava.common.event.Event;

public record PaymentEvent(String email) implements Event {
}