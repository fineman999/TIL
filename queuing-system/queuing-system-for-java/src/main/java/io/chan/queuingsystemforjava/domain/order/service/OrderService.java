package io.chan.queuingsystemforjava.domain.order.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final PerformanceRepository performanceRepository;
  private final SeatRepository seatRepository;
  private final SeatGradeRepository seatGradeRepository;

  @Transactional
  public OrderResponse createOrder(OrderRequest request, Member member) {
    // 공연 조회
    Performance performance =
        performanceRepository
            .findById(request.performanceId())
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));

    // 좌석 조회 및 락
    Seat seat =
        seatRepository
            .findByIdWithOptimistic(request.seatId())
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT));

    // 좌석 등급 조회

    final SeatGrade seatGrade = seatGradeRepository
            .findById(seat.getSeatGradeId())
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT_GRADE));



    // 좌석 상태를 확인하고 자신이 선점하고 있는지 확인
    seat.checkSeatStatusSelected(member);

    // 좌석 업데이트
    seat.markAsPendingPayment();

    // 주문 생성
    Order order =
        Order.create(
            request.orderId(),
            performance,
            seat,
            seatGrade.getPrice(),
            request.customerEmail(),
            request.customerName(),
            request.customerMobilePhone(),
            request.orderName(),
            OrderStatus.PENDING);

    // 주문 저장
    orderRepository.save(order);

    return OrderResponse.from(order.getId(), order.getOrderId(), order.getStatus().name());
  }
}
