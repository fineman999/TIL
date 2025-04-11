package io.chan.queuingsystemforjava.domain.payment.dto;

public record PaymentResponse(
        String mId,
        String paymentKey,
        String orderId,
        String orderName,
        String status,
        String requestedAt,
        String approvedAt,
        boolean useEscrow,
        String currency,
        long totalAmount,
        long balanceAmount,
        long suppliedAmount,
        long vat,
        long taxFreeAmount,
        String method,
        CardDetails card,
        EasyPayDetails easyPay,
        ReceiptDetails receipt,
        CheckoutDetails checkout
) {
    public record CardDetails(
            String issuerCode,
            String acquirerCode,
            String number,
            int installmentPlanMonths,
            boolean isInterestFree,
            String approveNo,
            String cardType,
            String ownerType,
            String acquireStatus,
            long amount
    ) {
    }

    public record EasyPayDetails(
            String provider,
            long amount,
            long discountAmount
    ) {
    }

    public record ReceiptDetails(
            String url
    ) {
    }

    public record CheckoutDetails(
            String url
    ) {
    }
}