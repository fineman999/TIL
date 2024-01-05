package io.chan.productorderservice.payment;

import io.chan.productorderservice.order.Order;
import io.chan.productorderservice.product.DiscountPolicy;
import io.chan.productorderservice.product.Product;

class PaymentAdapter implements PaymentPort {
    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    PaymentAdapter(PaymentGateway paymentGateway, PaymentRepository paymentRepository) {
        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Order getOrder(final Long orderId) {
        return new Order(new Product("상품명", 1000, DiscountPolicy.NONE), 1);
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
