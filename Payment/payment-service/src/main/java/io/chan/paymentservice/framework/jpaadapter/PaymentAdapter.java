package io.chan.paymentservice.framework.jpaadapter;

import io.chan.paymentservice.application.outputport.PaymentOutputPort;
import io.chan.paymentservice.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentAdapter implements PaymentOutputPort {
    private final PaymentJpaRepository paymentJpaRepository;
    @Override
    public void savePayment(Payment payment) {
        paymentJpaRepository.save(payment);
    }
}
