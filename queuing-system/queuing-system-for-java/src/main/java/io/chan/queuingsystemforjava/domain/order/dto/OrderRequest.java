package io.chan.queuingsystemforjava.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotBlank String orderId,
    @NotNull Long performanceId,
    @NotNull Long seatId,
    String customerEmail,
    String customerName,
    String customerMobilePhone,
    @NotBlank String orderName
) {}