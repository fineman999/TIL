package io.chan.productorderservice.payment.adapter.out.persistence;

import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.order.adapter.out.persistence.OrderRepository;

import io.chan.productorderservice.payment.adapter.out.external.PaymentGateway;
import io.chan.productorderservice.payment.application.port.PaymentPort;
import io.chan.productorderservice.payment.domain.Payment;
import org.springframework.stereotype.Component;

@Component
class PaymentAdapter implements PaymentPort {
    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    PaymentAdapter(PaymentGateway paymentGateway, PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrder(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("주문이 존재하지 않습니다.")
        );
    }

    @Override
    public void save(final Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void pay(final int price, final String cardNumber) {
        paymentGateway.execute(price, cardNumber);
    }
}
