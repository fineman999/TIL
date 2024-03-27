package io.chan.paymentservice.framework.web;

import io.chan.paymentservice.application.usecase.SaveOrderUseCase;
import io.chan.paymentservice.framework.web.dto.OrderInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final SaveOrderUseCase saveOrderUseCase;

    @PostMapping("/{memberId}")
    public ResponseEntity<Void> saveOrder(
            @PathVariable Long memberId,
            @RequestBody OrderInputDTO orderInputDTO
            ) {
        saveOrderUseCase.saveOrder(memberId, orderInputDTO);
        return ResponseEntity.ok().build();
    }
}
