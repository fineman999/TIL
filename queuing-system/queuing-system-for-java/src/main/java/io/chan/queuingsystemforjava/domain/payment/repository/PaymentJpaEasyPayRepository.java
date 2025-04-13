package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentEasyPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaEasyPayRepository extends JpaRepository<PaymentEasyPay, Long> {}
