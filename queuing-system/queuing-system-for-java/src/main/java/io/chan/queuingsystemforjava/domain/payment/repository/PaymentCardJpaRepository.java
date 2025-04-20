package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCardJpaRepository extends JpaRepository<PaymentCard, Long> {
    List<PaymentCard> findAllByPayment(Payment payment);
    // Custom query methods can be defined here if needed
}
