package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCancelJpaRepository extends JpaRepository<PaymentCancel, Long> {
}
