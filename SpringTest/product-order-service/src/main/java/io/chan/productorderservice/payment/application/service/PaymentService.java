package io.chan.productorderservice.payment.application.service;

import io.chan.productorderservice.order.domain.Order;
import io.chan.productorderservice.payment.application.port.PaymentPort;
import io.chan.productorderservice.payment.domain.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentPort paymentPort;

    PaymentService(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    @Transactional
    public ResponseEntity<Void> payment(
            final PaymentRequest request
    ) {
        final Order order = paymentPort.getOrder(request.orderId());

        final Payment payment = new Payment(order, request.cardNumber());
        paymentPort.pay(payment.getPrice(), payment.getCardNumber());
        paymentPort.save(payment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
