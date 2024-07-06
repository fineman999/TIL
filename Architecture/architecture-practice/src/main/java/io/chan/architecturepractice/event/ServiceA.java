package io.chan.architecturepractice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceA {
  private final ApplicationEventPublisher eventPublisher;

  public void doSomething() {
    log.info("ServiceA doSomething");
    eventPublisher.publishEvent(new EventB(this));
  }

  public void doSomething2() {
    log.info("ServiceA doSomething2");
    eventPublisher.publishEvent(new EventA(this));
  }
}
