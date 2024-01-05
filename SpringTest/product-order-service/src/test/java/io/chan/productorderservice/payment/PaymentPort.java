package io.chan.productorderservice.payment;

import io.chan.productorderservice.order.Order;

interface PaymentPort {
    Order getOrder(Long orderId);

    void save(Payment payment);

    void pay(int price, String cardNumber);
}
