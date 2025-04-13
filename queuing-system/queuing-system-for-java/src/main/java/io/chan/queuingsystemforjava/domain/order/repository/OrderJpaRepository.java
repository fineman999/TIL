package io.chan.queuingsystemforjava.domain.order.repository;

import io.chan.queuingsystemforjava.domain.order.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);
}