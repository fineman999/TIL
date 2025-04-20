package io.chan.queuingsystemforjava.domain.payment.repository;

import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.payment.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
}
