package io.chan.queuingsystemforjava.domain.ticket.dto.request;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;

import java.math.BigDecimal;

public record TicketCancelRequest(
    Long ticketId,
    String paymentKey,
    String cancelReason,
    BigDecimal cancelAmount,
    PaymentCancelRequest.RefundReceiveAccount refundReceiveAccount,
    BigDecimal taxFreeAmount
) {}