package io.chan.architecturepractice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class EventA extends ApplicationEvent {
    public EventA(final Object source) {
        super(source);
        log.info("EventA constructor");
    }
}
