package io.chan.queuingsystemforjava.support.mock;

import io.chan.queuingsystemforjava.common.event.Event;
import io.chan.queuingsystemforjava.common.event.EventPublisher;

import java.util.concurrent.atomic.AtomicInteger;

public class SpyEventPublisher implements EventPublisher {

    public AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void publish(Event event) {
        counter.incrementAndGet();
    }
}