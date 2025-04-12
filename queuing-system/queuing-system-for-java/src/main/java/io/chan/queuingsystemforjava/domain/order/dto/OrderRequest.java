package io.chan.queuingsystemforjava.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
    @NotBlank String orderId,
    @NotNull Long performanceId,
    @NotNull Long seatId,
    @NotNull @Positive BigDecimal amount,
    String customerEmail,
    String customerName,
    String customerMobilePhone,
    @NotBlank String orderName
) {}