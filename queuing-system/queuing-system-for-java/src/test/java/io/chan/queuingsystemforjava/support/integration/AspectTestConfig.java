package io.chan.queuingsystemforjava.support.integration;


import io.chan.queuingsystemforjava.domain.waitingsystem.aop.Debounce;
import io.chan.queuingsystemforjava.domain.waitingsystem.aop.Waiting;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@TestConfiguration
public class AspectTestConfig {

    @Bean
    public DebounceTarget debounceTarget() {
        return new DebounceTarget();
    }

    public static class DebounceTarget {

        private final AtomicInteger counter;

        public DebounceTarget() {
            counter = new AtomicInteger(0);
        }

        @Debounce
        public void increment() {
            counter.incrementAndGet();
        }

        public int get() {
            return counter.get();
        }
    }

    @RestController
    static class TestController {

        @Waiting
        @GetMapping("/api/v1/waiting/test")
        public ResponseEntity<String> test() {
            return ResponseEntity.ok("test");
        }
    }
}
