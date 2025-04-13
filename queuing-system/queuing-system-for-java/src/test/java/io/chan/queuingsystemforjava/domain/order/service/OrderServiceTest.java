package io.chan.queuingsystemforjava.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import io.chan.queuingsystemforjava.domain.order.dto.OrderRequest;
import io.chan.queuingsystemforjava.domain.order.dto.OrderResponse;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.SeatStatus;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        OrderService.class,
        OrderRepository.class,
        PerformanceRepository.class,
        SeatRepository.class,
        ZoneRepository.class,
        SeatGradeRepository.class
})
public class OrderServiceTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private SeatRepository seatRepository;

    private Member member;
    private Performance performance;
    private Zone zone;
    private SeatGrade seatGrade1;
    private Seat seat;


    @BeforeEach
    void setUpBase() {
        setUpMember();
        setUpPerformance();
        setUpZone();
        setUpSeatGrades();
        setUpSeat();
    }

    private void setUpMember() {
        member = Member.builder()
                .email("test@gmail.com")
                .password("testpassword")
                .memberRole(MemberRole.USER)
                .build();
        testEntityManager.persistAndFlush(member);
    }

    private void setUpPerformance() {
        performance = Performance.builder()
                .performanceName("공연")
                .performancePlace("장소")
                .performanceShowtime(
                        ZonedDateTime.of(2025, 4, 12, 14, 30, 0, 0, ZoneId.of("Asia/Seoul"))
                )
                .build();
        testEntityManager.persistAndFlush(performance);
    }

    private void setUpZone() {
        zone = Zone.builder()
                .zoneName("VIP")
                .performance(performance)
                .build();
        testEntityManager.persistAndFlush(zone);
    }
    private void setUpSeatGrades() {
        SeatGrade seatGrade1 =
                SeatGrade.builder()
                        .performance(performance)
                        .gradeName("Grade1")
                        .price(new BigDecimal("100.00"))
                        .build();
        testEntityManager.persistAndFlush(seatGrade1);

        this.seatGrade1 = seatGrade1;
    }

    private void setUpSeat() {
        // 좌석이 선택 된 상태로 생성
        seat = Seat.builder()
                .member(member)
                .seatCode("A01")
                .zone(zone)
                .seatStatus(SeatStatus.SELECTED)
                .seatGrade(seatGrade1)
                .seatGradeId(seatGrade1.getSeatGradeId())
                .build();

        testEntityManager.persistAndFlush(seat);
    }

    @Nested
    @DisplayName("createOrder 메서드를 호출할 때")
    class CreateOrderTest {
        private OrderRequest request;

        @BeforeEach
        void setUp() {
            request = new OrderRequest(
                    "ORDER_" + performance.getPerformanceId() + "_" + seat.getSeatId() + "_16987654321",
                    performance.getPerformanceId(),
                    seat.getSeatId(),
                    "customer@example.com",
                    "김고객",
                    "01012341234",
                    "공연 티켓 - 좌석 A01"
            );
        }

        @Test
        @DisplayName("주문이 성공적으로 생성된다")
        void createOrder_success() {
            // When
            OrderResponse response = orderService.createOrder(request, member);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.orderId()).isEqualTo(request.orderId());
            assertThat(response.status()).isEqualTo(OrderStatus.PENDING.name());

            // 주문 확인
            Order savedOrder = testEntityManager.find(Order.class, response.id());
            assertThat(savedOrder).isNotNull();
            assertThat(savedOrder.getOrderId()).isEqualTo(request.orderId());
            assertThat(savedOrder.getPerformance().getPerformanceId()).isEqualTo(request.performanceId());
            assertThat(savedOrder.getSeat().getSeatId()).isEqualTo(request.seatId());
            assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);

            // 좌석 상태 확인
            Seat updatedSeat = testEntityManager.find(Seat.class, seat.getSeatId());
            assertThat(updatedSeat.getSeatStatus()).isEqualTo(SeatStatus.PENDING_PAYMENT);

            // 금액 확인
            assertThat(updatedSeat.getSeatGrade().getPrice()).isEqualTo(seatGrade1.getPrice());
        }

        @Test
        @DisplayName("공연이 존재하지 않으면 예외가 발생한다")
        void createOrder_performanceNotFound() {
            // Given
            OrderRequest invalidRequest = new OrderRequest(
                    request.orderId(),
                    -1L, // 잘못된 공연 ID
                    request.seatId(),
                    request.customerEmail(),
                    request.customerName(),
                    request.customerMobilePhone(),
                    request.orderName()
            );

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class, () -> {
                orderService.createOrder(invalidRequest, member);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_PERFORMANCE);
        }

        @Test
        @DisplayName("좌석이 존재하지 않으면 예외가 발생한다")
        void createOrder_seatNotFound() {
            // Given
            OrderRequest invalidRequest = new OrderRequest(
                    request.orderId(),
                    request.performanceId(),
                    -1L, // 잘못된 좌석 ID
                    request.customerEmail(),
                    request.customerName(),
                    request.customerMobilePhone(),
                    request.orderName()
            );

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class, () -> {
                orderService.createOrder(invalidRequest, member);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_SEAT);
        }

        @Test
        @DisplayName("좌석이 구매 직전 상태이면 예외가 발생한다")
        void createOrder_seatNotAvailable() {
            // Given
            seat.markAsPendingPayment();
            testEntityManager.persistAndFlush(seat);

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class, () -> {
                orderService.createOrder(request, member);
            });
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_SEAT_STATUS);
        }
    }
}