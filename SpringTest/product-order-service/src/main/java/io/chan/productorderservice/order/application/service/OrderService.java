package io.chan.productorderservice.order.application.service;

import io.chan.productorderservice.order.application.port.OrderPort;
import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderPort orderPort;

    OrderService(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    @Transactional
    public void createOrder(
            final CreateOrderRequest request
    ) {
        final Product product = orderPort.getProductById(request.productId());
        final Order order = new Order(product, request.quantity());

        orderPort.save(order);
    }
}
