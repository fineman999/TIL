package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.payment.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaCardRepository extends JpaRepository<PaymentCard, Long> {
    // Custom query methods can be defined here if needed
}
