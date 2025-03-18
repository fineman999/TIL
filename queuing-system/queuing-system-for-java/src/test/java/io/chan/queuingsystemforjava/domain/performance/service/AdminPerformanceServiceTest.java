package io.chan.queuingsystemforjava.domain.performance.service;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.dto.CreatePerformanceRequest;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({PerformanceRepository.class})
class AdminPerformanceServiceTest {

    private AdminPerformanceService adminPerformanceService;

    @Autowired
    private PerformanceRepository performanceRepository;

    @BeforeEach
    void setUpBase() {
        adminPerformanceService = new AdminPerformanceService(performanceRepository);
    }

    @Nested
    @DisplayName("createPerformance 메서드를 호출할 때")
    class CreatePerformance {

        private CreatePerformanceRequest request;

        @BeforeEach
        void setUp() {
            request = new CreatePerformanceRequest("공연 이름", "공연 장소", ZonedDateTime.now());
        }

        @Test
        @DisplayName("공연이 성공적으로 생성된다.")
        void createPerformance_Success() {
            // When
            adminPerformanceService.createPerformance(request);

            // Then
            Optional<Performance> optionalPerformance =
                    performanceRepository.findAll().stream().findFirst();
            assertThat(optionalPerformance)
                    .isNotEmpty()
                    .get()
                    .satisfies(
                            performance -> {
                                assertThat(performance.getPerformanceName())
                                        .isEqualTo(request.performanceName());
                                assertThat(performance.getPerformancePlace())
                                        .isEqualTo(request.performanceLocation());
                            });
        }
    }
}
