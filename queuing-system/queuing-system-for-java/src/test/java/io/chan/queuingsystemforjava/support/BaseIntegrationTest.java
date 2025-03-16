package io.chan.queuingsystemforjava.support;

import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.global.security.JwtProviderImpl;
import io.chan.queuingsystemforjava.support.integration.AspectTestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import({AspectTestConfig.class, TestcontainersConfiguration.class}) // TestcontainersConfiguration 추가
public class BaseIntegrationTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtProvider jwtProvider() {
            // 원하는 값으로 JwtProviderImpl 생성
            return new JwtProviderImpl("test", 3600,3600, "thisisaverylongsecretkeyforjwtatleast32bytes!");
        }
        @Bean
        public AspectTestConfig.DebounceTarget debounceTarget() {
            return new AspectTestConfig.DebounceTarget();
        }
    }
}
