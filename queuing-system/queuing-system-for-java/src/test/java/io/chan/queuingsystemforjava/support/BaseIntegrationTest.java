package io.chan.queuingsystemforjava.support;

import io.chan.queuingsystemforjava.support.integration.AspectTestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;


@SpringBootTest
@AutoConfigureMockMvc
@Import({AspectTestConfig.class, TestcontainersConfiguration.class}) // TestcontainersConfiguration 추가
public class BaseIntegrationTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        public AspectTestConfig.DebounceTarget debounceTarget() {
            return new AspectTestConfig.DebounceTarget();
        }
    }
}
