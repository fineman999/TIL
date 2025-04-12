package io.chan.queuingsystemforjava.domain.order.repository;

import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    public Optional<Order> findByOrderId(String orderId) {
        return orderJpaRepository.findByOrderId(orderId);
    }

    public Optional<Seat> findSeatByIdWithLock(Long seatId) {
        return orderJpaRepository.findByIdWithLock(seatId);
    }

    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }
}