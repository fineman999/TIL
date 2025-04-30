package io.chan.queuingsystemforjava.domain.payment.processor;

import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketCancelRequest;

public interface PaymentProcessor {
    void processPayment(PaymentRequest paymentRequest);
    void cancelPayment(TicketCancelRequest cancelRequest, Payment payment);
}