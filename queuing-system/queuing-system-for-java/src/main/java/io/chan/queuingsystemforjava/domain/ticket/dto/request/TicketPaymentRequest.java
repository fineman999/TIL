package io.chan.queuingsystemforjava.domain.ticket.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketPaymentRequest(
    @NotNull Long seatId,
    @NotBlank String paymentKey,
    @NotBlank String orderId,
    @NotNull BigDecimal amount) {}
