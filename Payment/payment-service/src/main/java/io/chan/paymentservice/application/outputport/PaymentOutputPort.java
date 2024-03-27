package io.chan.paymentservice.application.outputport;

import io.chan.paymentservice.domain.Payment;

public interface PaymentOutputPort {
    void savePayment(Payment payment);
}
