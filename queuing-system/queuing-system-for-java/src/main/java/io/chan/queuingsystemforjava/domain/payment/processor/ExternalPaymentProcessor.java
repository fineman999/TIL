package io.chan.queuingsystemforjava.domain.payment.processor;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentPersistenceService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExternalPaymentProcessor implements PaymentProcessor {
  private final PaymentService paymentService;
  private final PaymentPersistenceService paymentPersistenceService;

  @Override
  public void processPayment(final PaymentRequest paymentRequest) {
    // 외부 결제 API 호출
    final PaymentResponse paymentResponse =
        paymentService.confirmPayment(
            paymentRequest.paymentKey(), paymentRequest.orderId(), paymentRequest.amount());
    // 결제 정보 저장
    paymentPersistenceService.savePayment(paymentResponse);
  }
}
