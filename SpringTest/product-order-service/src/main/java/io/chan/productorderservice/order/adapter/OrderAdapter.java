package io.chan.productorderservice.order.adapter;

import io.chan.productorderservice.order.application.port.OrderPort;
import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.product.domain.Product;
import io.chan.productorderservice.product.adapter.ProductRepository;
import org.springframework.stereotype.Component;

@Component
class OrderAdapter implements OrderPort {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private OrderAdapter(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Product getProductById(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    @Override
    public void save(final Order order) {
        orderRepository.save(order);
    }
}
