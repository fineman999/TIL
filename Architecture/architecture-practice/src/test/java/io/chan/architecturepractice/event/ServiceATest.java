package io.chan.architecturepractice.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceATest {

  @Autowired ServiceA serviceA;

  @Test
  void doSomething() {
    // When
    serviceA.doSomething();
    // serviceA.doSomething()를 호출하면
    // EventB 객체를 생성하고
    // 이벤트를 발행한다.
    // 이벤트를 발행하면
    // ServiceB의 handleEventB 메서드가 호출된다.

    serviceA.doSomething2();
  }
}
