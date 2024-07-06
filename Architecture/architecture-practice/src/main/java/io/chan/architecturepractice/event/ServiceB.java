package io.chan.architecturepractice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceB {
    @EventListener
    public void handleEventB(EventB eventB) {
        log.info("ServiceB handleEventB");
    }

    @EventListener
    public void handleEventA(EventA eventA) {
        log.info("ServiceB handleEventA");
    }
}
