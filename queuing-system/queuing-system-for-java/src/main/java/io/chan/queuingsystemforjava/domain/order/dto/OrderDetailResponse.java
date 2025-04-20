package io.chan.queuingsystemforjava.domain.order.dto;

import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.PaymentCard;
import io.chan.queuingsystemforjava.domain.payment.PaymentEasyPay;
import io.chan.queuingsystemforjava.domain.payment.PaymentVirtualAccount;
import io.chan.queuingsystemforjava.domain.seat.Seat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderDetailResponse(
        String orderId,
        String orderName,
        PerformanceInfo performance,
        SeatInfo seat,
        CustomerInfo customer,
        PaymentInfo payment,
        String status,
        ZonedDateTime createdAt
) {
  public record PerformanceInfo(
          String name,
          String place,
          ZonedDateTime showtime
  ) {}

  public record SeatInfo(
          String zone,
          String grade,
          String number
  ) {}

  public record CustomerInfo(
          String email,
          String name,
          String mobilePhone
  ) {}

  public record PaymentInfo(
          String paymentKey,
          String method,
          BigDecimal amount,
          String status,
          ZonedDateTime approvedAt
  ) {}

  public static OrderDetailResponse from(Order order, Payment payment, Seat seat) {
    return new OrderDetailResponse(
            order.getOrderId(),
            order.getOrderName(),
            new PerformanceInfo(
                    order.getPerformance().getPerformanceName(),
                    order.getPerformance().getPerformancePlace(),
                    order.getPerformance().getPerformanceShowtime()
            ),
            new SeatInfo(
                    seat.getZone().getZoneName(),
                    seat.getSeatGrade().getGradeName(),
                    seat.getSeatCode()
            ),
            new CustomerInfo(
                    order.getCustomerEmail(),
                    order.getCustomerName(),
                    order.getCustomerMobilePhone()
            ),
            new PaymentInfo(
                    payment.getPaymentKey(),
                    payment.getMethod(),
                    payment.getTotalAmount(),
                    payment.getStatus(),
                    payment.getApprovedAt()
            ),
            order.getStatus().name(),
            order.getCreatedAt()
    );
  }
}

