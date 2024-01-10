package io.chan.productorderservice.payment.adapter.in;


import io.chan.productorderservice.payment.application.service.PaymentRequest;
import io.chan.productorderservice.payment.application.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> payment(
            @RequestBody final PaymentRequest request
    ) {
        paymentService.payment(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
