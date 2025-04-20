package io.chan.queuingsystemforjava.domain.order.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.repository.OrderJpaRepository;
import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.order.dto.OrderDetailResponse;
import io.chan.queuingsystemforjava.domain.order.dto.OrderListResponse;
import io.chan.queuingsystemforjava.domain.order.dto.OrderSearchRequest;
import io.chan.queuingsystemforjava.domain.payment.repository.PaymentJpaRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {
  private final OrderJpaRepository orderJpaRepository;
  private final PaymentJpaRepository paymentJpaRepository;
  private final SeatRepository seatRepository;

  public Page<OrderListResponse> getOrders(
          Member member,
          OrderSearchRequest searchRequest,
          Pageable pageable) {
    return orderJpaRepository
            .findOrdersByMember(
                    member,
                    searchRequest.startDate(),
                    searchRequest.endDate(),
                    searchRequest.orderName(),
                    searchRequest.status(),
                    pageable)
            .map(OrderListResponse::from);
  }

  public OrderDetailResponse getOrderDetail(Member member, String orderId) {
    Order order = orderJpaRepository.findByOrderIdWithDetails(orderId)
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_ORDER));

    validateOrderOwnership(member, order);

    Payment payment = paymentJpaRepository.findByOrder(order)
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PAYMENT));

    Seat seat = seatRepository.findById(order.getSeatId())
            .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT));

    return OrderDetailResponse.from(order, payment, seat);
  }

  private void validateOrderOwnership(Member member, Order order) {
    if (!order.getMember().equals(member)) {
      throw new TicketingException(ErrorCode.FORBIDDEN);
    }
  }
}
