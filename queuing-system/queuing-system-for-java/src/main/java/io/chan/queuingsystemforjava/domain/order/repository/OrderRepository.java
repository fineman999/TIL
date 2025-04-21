package io.chan.queuingsystemforjava.domain.order.repository;

import io.chan.queuingsystemforjava.domain.order.Order;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    public Optional<Order> findByOrderId(String orderId) {
        return orderJpaRepository.findByOrderId(orderId);
    }

    public Optional<Order> findByOrderIdWithPessimistic(String orderId) {
        return orderJpaRepository.findByOrderIdWithPessimistic(orderId);
    }

    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    public Optional<Order> findByIdWithPessimistic(Long id) {
        return orderJpaRepository.findByIdWithPessimistic(id);
    }
}