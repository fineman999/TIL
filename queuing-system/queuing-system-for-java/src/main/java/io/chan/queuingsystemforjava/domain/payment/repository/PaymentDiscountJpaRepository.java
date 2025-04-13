package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDiscountJpaRepository extends JpaRepository<PaymentDiscount, Long> {}
