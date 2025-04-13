package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransferJpaRepository extends JpaRepository<PaymentTransfer, Long> {}
