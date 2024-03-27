package io.chan.paymentservice.framework.web;

import com.siot.IamportRestClient.response.Payment;
import io.chan.paymentservice.application.usecase.CallBackIamportPaymentUseCase;
import io.chan.paymentservice.application.usecase.InQueryPaymentUseCase;
import io.chan.paymentservice.framework.web.dto.PaymentCallbackInputDTO;
import io.chan.paymentservice.framework.web.dto.RequestPayOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final CallBackIamportPaymentUseCase callBackIamportPaymentUseCase;
    private final InQueryPaymentUseCase inQueryPaymentUseCase;

    @GetMapping("/{impUid}")
    public ResponseEntity<RequestPayOutputDTO> inQueryPayment(
            @PathVariable String impUid
    ) {
        RequestPayOutputDTO payOutputDTO = inQueryPaymentUseCase.inQueryPayment(impUid);
        return ResponseEntity.ok(payOutputDTO);
    }
    @PostMapping
    public ResponseEntity<Payment> callbackPayment(
            @RequestBody PaymentCallbackInputDTO paymentCallbackInputDTO
    ) {
         Payment payment = callBackIamportPaymentUseCase.callbackPayment(paymentCallbackInputDTO);
         return ResponseEntity.ok(payment);
     }
}
