package io.chan.queuingsystemforjava.domain.zone.service;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneElement;
import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneRequest;
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

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ZoneRepository.class, PerformanceRepository.class})
public class AdminZoneServiceTest {
    private AdminZoneService adminZoneService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired private ZoneRepository zoneRepository;

    @Autowired private PerformanceRepository performanceRepository;

    @BeforeEach
    void setUpBase() {
        adminZoneService = new AdminZoneService(zoneRepository, performanceRepository);
    }

    @Nested
    @DisplayName("createZones 메서드를 호출할 때")
    class CreateZonesTest {

        private Long performanceId;
        private CreateZoneRequest zoneCreationRequest;

        @BeforeEach
        void setUp() {
            Performance performance =
                    Performance.builder()
                            .performanceName("공연 이름")
                            .performancePlace("공연 장소")
                            .performanceShowtime(ZonedDateTime.now())
                            .build();
            performance = testEntityManager.persistAndFlush(performance);
            performanceId = performance.getPerformanceId();

            CreateZoneElement createZoneElement = new CreateZoneElement("VIP");
            CreateZoneElement createZoneElement2 = new CreateZoneElement("General");

            zoneCreationRequest = new CreateZoneRequest(List.of(createZoneElement, createZoneElement2));
        }

        @Test
        @DisplayName("존이 성공적으로 생성된다.")
        void createZones_Success() {
            // When
            adminZoneService.createZones(performanceId, zoneCreationRequest);

            // Then
            List<Zone> zones = zoneRepository.findAll();
            assertThat(zones).hasSize(2);
            assertThat(zones.get(0).getZoneName()).isEqualTo("VIP");
            assertThat(zones.get(1).getZoneName()).isEqualTo("General");
        }
    }
}