package io.chan.queuingsystemforjava.domain.payment.processor;

import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCancellationService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentConfirmationService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCreationService;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketCancelRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExternalPaymentProcessor implements PaymentProcessor {
  private final PaymentConfirmationService paymentConfirmationService;
  private final PaymentCreationService paymentCreationService;
  private final PaymentCancellationService paymentCancellationService;
  private final IdempotencyRedisRepository idempotencyRedisRepository;
  @Override
  public void processPayment(final PaymentRequest paymentRequest) {
    // 외부 결제 API 호출
    final PaymentResponse paymentResponse =
        paymentConfirmationService.confirmPayment(
            paymentRequest.paymentKey(), paymentRequest.orderId(), paymentRequest.amount());
    // 결제 정보 저장
    paymentCreationService.savePayment(paymentResponse);
  }

  @Override
  public void cancelPayment(TicketCancelRequest cancelRequest, Payment payment) {

    PaymentCancelRequest paymentCancelRequest = new PaymentCancelRequest(
            cancelRequest.cancelReason(),
            cancelRequest.cancelAmount(),
            cancelRequest.refundReceiveAccount() != null ? new PaymentCancelRequest.RefundReceiveAccount(
                    cancelRequest.refundReceiveAccount().bank(),
                    cancelRequest.refundReceiveAccount().accountNumber(),
                    cancelRequest.refundReceiveAccount().holderName()
            ) : null,
            cancelRequest.taxFreeAmount(),
            payment.getCurrency()
    );

    String idempotencyKey = idempotencyRedisRepository.generateIdempotencyKey();

    PaymentCancelResponse cancelResponse = paymentCancellationService.cancelPayment(
            cancelRequest.paymentKey(),
            paymentCancelRequest,
            idempotencyKey
    );
    // 취소내역 저장
    paymentCreationService.savePaymentCancel(cancelResponse, payment);
  }
}
