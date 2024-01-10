package io.chan.productorderservice.order.adapter.in;

import io.chan.productorderservice.order.application.service.CreateOrderRequest;
import io.chan.productorderservice.order.application.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderPort;

    public OrderController(OrderService orderPort) {
        this.orderPort = orderPort;
    }


    @PostMapping
    public ResponseEntity<Void> createOrder(
            @RequestBody final CreateOrderRequest request
    ) {
        orderPort.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
