package io.chan.queuingsystemforjava.domain.payment.dto;

import java.math.BigDecimal;

public record PaymentCancelRequest(
    String cancelReason,
    BigDecimal cancelAmount,
    RefundReceiveAccount refundReceiveAccount,
    BigDecimal taxFreeAmount,
    String currency
) {
    public record RefundReceiveAccount(
        String bank,
        String accountNumber,
        String holderName
    ) {}
}