package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.common.event.SpringEventPublisher;
import io.chan.queuingsystemforjava.domain.waitingsystem.listener.WaitingSystemEventListener;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher eventPublisher) {
        return new SpringEventPublisher(eventPublisher);
    }


    @Bean
    public WaitingSystemEventListener waitingEventListener(WaitingSystem waitingSystem) {
        return new WaitingSystemEventListener(waitingSystem);
    }
}