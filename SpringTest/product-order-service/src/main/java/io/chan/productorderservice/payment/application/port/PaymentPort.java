package io.chan.productorderservice.payment.application.port;

import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.payment.domain.Payment;

public interface PaymentPort {
    Order getOrder(Long orderId);

    void save(Payment payment);

    void pay(int price, String cardNumber);
}
