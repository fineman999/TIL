package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.PaymentEasyPay;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentEasyPayJpaRepository extends JpaRepository<PaymentEasyPay, Long> {
    List<PaymentEasyPay> findAllByPayment(Payment payment);
}
