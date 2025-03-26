package io.chan.queuingsystemforjava.domain.waitingsystem.dto;

import io.chan.queuingsystemforjava.common.event.Event;


public record LastPollingEvent(String email, long performanceId) implements Event {

}