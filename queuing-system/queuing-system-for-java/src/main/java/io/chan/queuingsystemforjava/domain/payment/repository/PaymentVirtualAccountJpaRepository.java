package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentVirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentVirtualAccountJpaRepository extends JpaRepository<PaymentVirtualAccount, Long> {}
