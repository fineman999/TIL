package io.chan.queuingsystemforjava.domain.waitingsystem.dto;

import io.chan.queuingsystemforjava.common.event.Event;

public record PollingEvent(long performanceId) implements Event {

}