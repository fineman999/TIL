package io.chan.paymentservice.framework.jpaadapter;

import io.chan.paymentservice.application.outputport.OrderOutputPort;
import io.chan.paymentservice.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderAdapter implements OrderOutputPort {
    private final OrderJpaRepository orderJpaRepository;
    @Override
    public void saveOrder(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public Order getOrderAndPaymentAndMemberByOrderUid(String orderUid) {
        return orderJpaRepository.findOrderAndPaymentAndMemberByOrderUid(orderUid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order getOrderAndPayment(String orderUid) {
        return orderJpaRepository.findOrderAndPaymentByOrderUid(orderUid)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
