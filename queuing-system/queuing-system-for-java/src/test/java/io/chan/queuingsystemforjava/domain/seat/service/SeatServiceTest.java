package io.chan.queuingsystemforjava.domain.seat.service;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatElement;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatGradeElement;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({PerformanceRepository.class, ZoneRepository.class, SeatGradeRepository.class, SeatRepository.class})
public class SeatServiceTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired private PerformanceRepository performanceRepository;
    @Autowired private ZoneRepository zoneRepository;
    @Autowired private SeatGradeRepository seatGradeRepository;
    @Autowired private SeatRepository seatRepository;

    private SeatService seatService;

    private Member member;
    private Performance performance;
    private Zone zone;
    private SeatGrade seatGrade1;
    private SeatGrade seatGrade2;

    @BeforeEach
    void setUpBase() {
        seatService =
                new SeatService(
                        performanceRepository, zoneRepository, seatGradeRepository, seatRepository);
        setUpMember();
        setUpPerformance();
        setUpZone();
        setUpSeatGrades();
    }

    private void setUpMember() {
        Member member =
                Member.builder()
                        .email("test@gmail.com")
                        .password("testpassword")
                        .memberRole(MemberRole.USER)
                        .build();

        testEntityManager.persist(member);
        this.member = member;
    }

    private void setUpPerformance() {
        Performance performance =
                Performance.builder()
                        .performanceName("공연")
                        .performancePlace("장소")
                        .performanceShowtime(
                                ZonedDateTime.of(
                                        2024, 8, 23, 14, 30, 0, 0, ZoneId.of("Asia/Seoul")))
                        .build();
        testEntityManager.persistAndFlush(performance);
        this.performance = performance;
    }

    private void setUpZone() {
        Zone zone = Zone.builder().zoneName("VIP").performance(performance).build();
        testEntityManager.persistAndFlush(zone);
        this.zone = zone;
    }

    private void setUpSeatGrades() {
        SeatGrade seatGrade1 =
                SeatGrade.builder()
                        .performance(performance)
                        .price(BigDecimal.valueOf(10000L))
                        .gradeName("Grade1")
                        .build();
        SeatGrade seatGrade2 =
                SeatGrade.builder()
                        .performance(performance)
                        .price(BigDecimal.valueOf(20000L))
                        .gradeName("Grade2")
                        .build();
        testEntityManager.persistAndFlush(seatGrade1);
        testEntityManager.persistAndFlush(seatGrade2);

        this.seatGrade1 = seatGrade1;
        this.seatGrade2 = seatGrade2;
    }

    @Nested
    @DisplayName("getSeats 메소드를 호출할 때")
    class GetZonesTest {
        private Seat seat1;
        private Seat seat2;

        @BeforeEach
        void setUp() {
            seat1 =
                    Seat.builder()
                            .member(member)
                            .seatCode("A01")
                            .zone(zone)
                            .seatGrade(seatGrade1)
                            .seatGradeId(seatGrade1.getSeatGradeId())
                            .build();

            seat2 =
                    Seat.builder()
                            .member(member)
                            .seatCode("A01")
                            .zone(zone)
                            .seatGrade(seatGrade2)
                            .seatGradeId(seatGrade2.getSeatGradeId())
                            .build();
        }

        @Test
        @DisplayName("좌석 목록이 성공적으로 생성된다.")
        void getZones_success() {
            // Given
            testEntityManager.persistAndFlush(seat1);
            testEntityManager.persistAndFlush(seat2);

            // When
            ItemResult<SeatElement> seats = seatService.getSeats(zone.getZoneId());

            // Then
            List<SeatElement> seatElements = seats.getItems();
            SeatElement seatElement1 = seatElements.get(0);
            SeatElement seatElement2 = seatElements.get(1);

            assertThat(seatElements).hasSize(2);
            assertSeatElement(seatElement1, seat1);
            assertSeatElement(seatElement2, seat2);
        }

        private void assertSeatElement(SeatElement seatElement, Seat expectedSeat) {
            assertThat(seatElement.seatId()).isEqualTo(expectedSeat.getSeatId());
            assertThat(seatElement.seatCode()).isEqualTo(expectedSeat.getSeatCode());
            assertThat(seatElement.seatAvailable()).isEqualTo(expectedSeat.isSelectable());
            assertThat(seatElement.price()).isEqualTo(expectedSeat.getSeatGrade().getPrice());
            assertThat(seatElement.gradeName()).isEqualTo(expectedSeat.getSeatGrade().getGradeName());
        }
    }

    @Nested
    @DisplayName("getSeatGrades 메소드를 호출할 때")
    class GetSeatGradesTest {

        @Test
        @DisplayName("좌석 등급 목록이 성공적으로 반환된다.")
        void getSeatGrades_success() {
            // Given
            Long performanceId = performance.getPerformanceId();

            // When
            ItemResult<SeatGradeElement> seatGradesResult =
                    seatService.getSeatGrades(performanceId);

            // Then
            List<SeatGradeElement> seatGradeElements = seatGradesResult.getItems();
            SeatGradeElement seatGradeElement1 = seatGradeElements.get(0);
            SeatGradeElement seatGradeElement2 = seatGradeElements.get(1);

            assertThat(seatGradeElements).hasSize(2);
            assertSeatGradeElement(seatGradeElement1, seatGrade1);
            assertSeatGradeElement(seatGradeElement2, seatGrade2);
        }

        private void assertSeatGradeElement(
                SeatGradeElement seatGradeElement, SeatGrade expectedSeatGrade) {
            assertThat(seatGradeElement.seatGradeId()).isEqualTo(expectedSeatGrade.getSeatGradeId());
            assertThat(seatGradeElement.gradeName()).isEqualTo(expectedSeatGrade.getGradeName());
            assertThat(seatGradeElement.price()).isEqualTo(expectedSeatGrade.getPrice());
        }
    }
}