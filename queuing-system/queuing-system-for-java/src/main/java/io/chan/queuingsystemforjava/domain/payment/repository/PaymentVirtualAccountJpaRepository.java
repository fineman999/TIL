package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.PaymentVirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentVirtualAccountJpaRepository extends JpaRepository<PaymentVirtualAccount, Long> {
    List<PaymentVirtualAccount> findAllByPayment(Payment payment);
}
