package io.chan.queuingsystemforjava.domain.zone.service;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.performance.service.UserPerformanceService;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.dto.ZoneElement;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ZoneRepository.class, PerformanceRepository.class})
class UserZoneServiceTest {

    private UserZoneService userZoneService;

    @Autowired private TestEntityManager testEntityManager;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired private PerformanceRepository performanceRepository;

    @BeforeEach
    void setUp() {
        userZoneService = new UserZoneService(zoneRepository, performanceRepository);
    }

    @Nested
    @DisplayName("getZones 메서드를 호출할 때")
    class GetZones {

        @Test
        @DisplayName("특정 공연의 구역을 성공적으로 조회한다.")
        void getZones_Success() {
            // Given
            ZonedDateTime showtime =
                    ZonedDateTime.of(2024, 8, 23, 14, 30, 0, 0, ZoneId.of("Asia/Seoul"));

            Performance performance =
                    Performance.builder()
                            .performanceName("테스트 공연")
                            .performancePlace("테스트 장소")
                            .performanceShowtime(showtime)
                            .build();
            Zone zone1 = Zone.builder().performance(performance).zoneName("A구역").build();
            Zone zone2 = Zone.builder().performance(performance).zoneName("B구역").build();

            testEntityManager.persist(performance);
            testEntityManager.persist(zone1);
            testEntityManager.persist(zone2);
            testEntityManager.flush();

            // When
            ItemResult<ZoneElement> zoneElements =
                    userZoneService.getZones(performance.getPerformanceId());

            // Then
            assertThat(zoneElements.getItems())
                    .isNotEmpty()
                    .hasSize(2)
                    .satisfies(
                            elements -> {
                                assertThat(elements.get(0))
                                        .satisfies(
                                                element -> {
                                                    assertThat(element.id()).isNotNull();
                                                    assertThat(element.zoneName())
                                                            .isEqualTo("A구역");
                                                });
                                assertThat(elements.get(1))
                                        .satisfies(
                                                element -> {
                                                    assertThat(element.id()).isNotNull();
                                                    assertThat(element.zoneName())
                                                            .isEqualTo("B구역");
                                                });
                            });
        }
    }
}