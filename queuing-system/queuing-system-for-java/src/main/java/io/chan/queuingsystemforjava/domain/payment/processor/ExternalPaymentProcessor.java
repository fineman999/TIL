package io.chan.queuingsystemforjava.domain.payment.processor;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentConfirmationService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCreationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExternalPaymentProcessor implements PaymentProcessor {
  private final PaymentConfirmationService paymentConfirmationService;
  private final PaymentCreationService paymentCreationService;

  @Override
  public void processPayment(final PaymentRequest paymentRequest) {
    // 외부 결제 API 호출
    final PaymentResponse paymentResponse =
        paymentConfirmationService.confirmPayment(
            paymentRequest.paymentKey(), paymentRequest.orderId(), paymentRequest.amount());
    // 결제 정보 저장
    paymentCreationService.savePayment(paymentResponse);
  }
}
