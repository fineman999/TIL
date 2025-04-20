package io.chan.queuingsystemforjava.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import io.chan.queuingsystemforjava.domain.order.dto.OrderDetailResponse;
import io.chan.queuingsystemforjava.domain.order.dto.OrderListResponse;
import io.chan.queuingsystemforjava.domain.order.dto.OrderSearchRequest;
import io.chan.queuingsystemforjava.domain.order.repository.OrderJpaRepository;
import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.repository.PaymentJpaRepository;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderQueryService 테스트")
class OrderQueryServiceTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private OrderQueryService orderQueryService;

    private Member member;
    private Order order;
    private Payment payment;
    private Seat seat;
    private Performance performance;
    private OrderSearchRequest searchRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        member = Member.builder()
                .memberId(1L)
                .email("test@example.com")
                .build();

        performance = Performance.builder()
                .performanceId(1L)
                .performanceName("테스트 공연")
                .performancePlace("테스트 장소")
                .performanceShowtime(ZonedDateTime.now().plusDays(1))
                .build();

        Zone zone = Zone.builder()
                .zoneName("VIP")
                .build();

        SeatGrade seatGrade = SeatGrade.builder()
                .gradeName("VIP")
                .build();

        seat = Seat.builder()
                .seatId(1L)
                .zone(zone)
                .seatGrade(seatGrade)
                .seatCode("A-12")
                .build();

        order = Order.builder()
                .id(1L)
                .orderId("ORDER123")
                .member(member)
                .performance(performance)
                .seat(seat)
                .seatId(seat.getSeatId())
                .orderName("테스트 공연 - A-12")
                .amount(new BigDecimal("100.00"))
                .customerEmail("test@example.com")
                .customerName("테스트 사용자")
                .customerMobilePhone("010-1234-5678")
                .status(OrderStatus.COMPLETED)
                .build();
        order.setCreatedAt(ZonedDateTime.now());

        payment = Payment.builder()
                .id(1L)
                .order(order)
                .paymentKey("PAYMENT123")
                .method("CARD")
                .totalAmount(new BigDecimal("100.00"))
                .status("APPROVED")
                .approvedAt(ZonedDateTime.now())
                .build();

        searchRequest = new OrderSearchRequest(
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                "테스트 공연",
                OrderStatus.COMPLETED
        );

        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Nested
    @DisplayName("getOrders 메서드 테스트")
    class GetOrdersTest {

        @Test
        @DisplayName("주문 목록을 성공적으로 조회한다")
        void getOrders_success() {
            // Given
            Page<Order> orderPage = new PageImpl<>(List.of(order), pageable, 1);
            when(orderJpaRepository.findOrdersByMember(
                    eq(member),
                    eq(searchRequest.startDate()),
                    eq(searchRequest.endDate()),
                    eq(searchRequest.orderName()),
                    eq(searchRequest.status()),
                    eq(pageable)
            )).thenReturn(orderPage);

            // When
            Page<OrderListResponse> result = orderQueryService.getOrders(member, searchRequest, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            OrderListResponse response = result.getContent().get(0);
            assertThat(response.orderId()).isEqualTo(order.getOrderId());
            assertThat(response.orderName()).isEqualTo(order.getOrderName());
            assertThat(response.performanceName()).isEqualTo(performance.getPerformanceName());
            assertThat(response.performancePlace()).isEqualTo(performance.getPerformancePlace());
            assertThat(response.performanceShowtime()).isEqualTo(performance.getPerformanceShowtime());
            assertThat(response.amount()).isEqualTo(order.getAmount());
            assertThat(response.status()).isEqualTo(order.getStatus().name());
            assertThat(response.createdAt()).isEqualTo(order.getCreatedAt());

            verify(orderJpaRepository).findOrdersByMember(
                    eq(member),
                    eq(searchRequest.startDate()),
                    eq(searchRequest.endDate()),
                    eq(searchRequest.orderName()),
                    eq(searchRequest.status()),
                    eq(pageable)
            );
        }

        @Test
        @DisplayName("주문이 없으면 빈 페이지가 반환된다")
        void getOrders_emptyResult() {
            // Given
            Page<Order> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            when(orderJpaRepository.findOrdersByMember(
                    eq(member),
                    eq(searchRequest.startDate()),
                    eq(searchRequest.endDate()),
                    eq(searchRequest.orderName()),
                    eq(searchRequest.status()),
                    eq(pageable)
            )).thenReturn(emptyPage);

            // When
            Page<OrderListResponse> result = orderQueryService.getOrders(member, searchRequest, pageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getContent()).isEmpty();

            verify(orderJpaRepository).findOrdersByMember(
                    eq(member),
                    eq(searchRequest.startDate()),
                    eq(searchRequest.endDate()),
                    eq(searchRequest.orderName()),
                    eq(searchRequest.status()),
                    eq(pageable)
            );
        }
    }

    @Nested
    @DisplayName("getOrderDetail 메서드 테스트")
    class GetOrderDetailTest {

        @Test
        @DisplayName("주문 상세 정보를 성공적으로 조회한다")
        void getOrderDetail_success() {
            // Given
            when(orderJpaRepository.findByOrderIdWithDetails("ORDER123")).thenReturn(Optional.of(order));
            when(paymentJpaRepository.findByOrder(order)).thenReturn(Optional.of(payment));
            when(seatRepository.findById(order.getSeatId())).thenReturn(Optional.of(seat));

            // When
            OrderDetailResponse result = orderQueryService.getOrderDetail(member, "ORDER123");

            // Then
            assertThat(result).isNotNull();
            assertThat(result.orderId()).isEqualTo(order.getOrderId());
            assertThat(result.orderName()).isEqualTo(order.getOrderName());
            assertThat(result.performance().name()).isEqualTo(performance.getPerformanceName());
            assertThat(result.performance().place()).isEqualTo(performance.getPerformancePlace());
            assertThat(result.performance().showtime()).isEqualTo(performance.getPerformanceShowtime());
            assertThat(result.seat().zone()).isEqualTo(seat.getZone().getZoneName());
            assertThat(result.seat().grade()).isEqualTo(seat.getSeatGrade().getGradeName());
            assertThat(result.seat().number()).isEqualTo(seat.getSeatCode());
            assertThat(result.customer().email()).isEqualTo(order.getCustomerEmail());
            assertThat(result.customer().name()).isEqualTo(order.getCustomerName());
            assertThat(result.customer().mobilePhone()).isEqualTo(order.getCustomerMobilePhone());
            assertThat(result.payment().paymentKey()).isEqualTo(payment.getPaymentKey());
            assertThat(result.payment().method()).isEqualTo(payment.getMethod());
            assertThat(result.payment().amount()).isEqualTo(payment.getTotalAmount());
            assertThat(result.payment().status()).isEqualTo(payment.getStatus());
            assertThat(result.payment().approvedAt()).isEqualTo(payment.getApprovedAt());
            assertThat(result.status()).isEqualTo(order.getStatus().name());
            assertThat(result.createdAt()).isEqualTo(order.getCreatedAt());

            verify(orderJpaRepository).findByOrderIdWithDetails("ORDER123");
            verify(paymentJpaRepository).findByOrder(order);
            verify(seatRepository).findById(order.getSeatId());
        }

        @Test
        @DisplayName("주문이 존재하지 않으면 예외가 발생한다")
        void getOrderDetail_orderNotFound() {
            // Given
            when(orderJpaRepository.findByOrderIdWithDetails("INVALID_ORDER")).thenReturn(Optional.empty());

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class,
                    () -> orderQueryService.getOrderDetail(member, "INVALID_ORDER"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);

            verify(orderJpaRepository).findByOrderIdWithDetails("INVALID_ORDER");
            verifyNoInteractions(paymentJpaRepository, seatRepository);
        }

        @Test
        @DisplayName("권한이 없는 사용자는 예외가 발생한다")
        void getOrderDetail_unauthorizedMember() {
            // Given
            Member unauthorizedMember = Member.builder()
                    .memberId(2L)
                    .email("other@example.com")
                    .build();
            when(orderJpaRepository.findByOrderIdWithDetails("ORDER123")).thenReturn(Optional.of(order));

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class,
                    () -> orderQueryService.getOrderDetail(unauthorizedMember, "ORDER123"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);

            verify(orderJpaRepository).findByOrderIdWithDetails("ORDER123");
            verifyNoInteractions(paymentJpaRepository, seatRepository);
        }

        @Test
        @DisplayName("결제가 존재하지 않으면 예외가 발생한다")
        void getOrderDetail_paymentNotFound() {
            // Given
            when(orderJpaRepository.findByOrderIdWithDetails("ORDER123")).thenReturn(Optional.of(order));
            when(paymentJpaRepository.findByOrder(order)).thenReturn(Optional.empty());

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class,
                    () -> orderQueryService.getOrderDetail(member, "ORDER123"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_PAYMENT);

            verify(orderJpaRepository).findByOrderIdWithDetails("ORDER123");
            verify(paymentJpaRepository).findByOrder(order);
            verifyNoInteractions(seatRepository);
        }

        @Test
        @DisplayName("좌석이 존재하지 않으면 예외가 발생한다")
        void getOrderDetail_seatNotFound() {
            // Given
            when(orderJpaRepository.findByOrderIdWithDetails("ORDER123")).thenReturn(Optional.of(order));
            when(paymentJpaRepository.findByOrder(order)).thenReturn(Optional.of(payment));
            when(seatRepository.findById(order.getSeatId())).thenReturn(Optional.empty());

            // When & Then
            TicketingException exception = assertThrows(TicketingException.class,
                    () -> orderQueryService.getOrderDetail(member, "ORDER123"));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_SEAT);

            verify(orderJpaRepository).findByOrderIdWithDetails("ORDER123");
            verify(paymentJpaRepository).findByOrder(order);
            verify(seatRepository).findById(order.getSeatId());
        }
    }
}