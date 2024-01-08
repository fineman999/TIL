package io.chan.appleinapptest.controller;

import io.chan.appleinapptest.UserReceiptRequest;
import io.chan.appleinapptest.service.AppleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppleController {

    private final AppleService appleService;

    public AppleController(AppleService appleService) {
        this.appleService = appleService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(
            @RequestBody @Valid UserReceiptRequest userReceiptRequest
    ) {
        String code = appleService.updatePurchaseHistory(userReceiptRequest);

        return ResponseEntity.ok(code);
    }
}
