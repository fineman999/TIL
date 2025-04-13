package io.chan.queuingsystemforjava.domain.payment.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record PaymentResponse(
    String version,
    String paymentKey,
    String type,
    String orderId,
    String orderName,
    String mId,
    String currency,
    String method,
    BigDecimal totalAmount,
    BigDecimal balanceAmount,
    String status,
    OffsetDateTime requestedAt,
    OffsetDateTime approvedAt,
    boolean useEscrow,
    String lastTransactionKey,
    BigDecimal suppliedAmount,
    BigDecimal vat,
    boolean cultureExpense,
    BigDecimal taxFreeAmount,
    Integer taxExemptionAmount,
    List<Cancel> cancels,
    Card card,
    VirtualAccount virtualAccount,
    MobilePhone mobilePhone,
    GiftCertificate giftCertificate,
    Transfer transfer,
    EasyPay easyPay,
    CashReceipt cashReceipt,
    List<CashReceipt> cashReceipts,
    Discount discount,
    Receipt receipt,
    Checkout checkout) {
  public record Receipt(String url) {}

  public record Checkout(String url) {}

  public record Cancel(
      BigDecimal cancelAmount,
      String cancelReason,
      BigDecimal taxFreeAmount,
      Integer taxExemptionAmount,
      BigDecimal refundableAmount,
      BigDecimal transferDiscountAmount,
      BigDecimal easyPayDiscountAmount,
      OffsetDateTime canceledAt,
      String transactionKey,
      String receiptKey,
      String cancelStatus,
      String cancelRequestId,
      boolean isPartialCancelable) {}

  public record Card(
      BigDecimal amount,
      String issuerCode,
      String acquirerCode,
      String number,
      Integer installmentPlanMonths,
      String approveNo,
      boolean useCardPoint,
      String cardType,
      String ownerType,
      String acquireStatus,
      boolean isInterestFree,
      String interestPayer) {}

  public record VirtualAccount(
      String accountType,
      String accountNumber,
      String bankCode,
      String customerName,
      String dueDate,
      String refundStatus,
      boolean expired,
      String settlementStatus,
      RefundReceiveAccount refundReceiveAccount,
      String secret) {
    public record RefundReceiveAccount(String bankCode, String accountNumber, String holderName) {}
  }

  public record MobilePhone(
      String customerMobilePhone, String settlementStatus, String receiptUrl) {}

  public record GiftCertificate(String approveNo, String settlementStatus) {}

  public record Transfer(String bankCode, String settlementStatus) {}

  public record EasyPay(String provider, BigDecimal amount, BigDecimal discountAmount) {}

  public record CashReceipt(
      String type,
      String receiptKey,
      String issueNumber,
      String receiptUrl,
      BigDecimal amount,
      BigDecimal taxFreeAmount) {}

  public record Discount(Integer amount) {}
}
