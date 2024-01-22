package io.chan.appleinapptest.controller;

import com.apple.itunes.storekit.model.CheckTestNotificationResponse;
import com.apple.itunes.storekit.model.ResponseBodyV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.SendTestNotificationResponse;
import io.chan.appleinapptest.UserReceiptRequest;
import io.chan.appleinapptest.service.AppleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AppleService appleService;


    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(
            @RequestBody @Valid UserReceiptRequest userReceiptRequest
    ) {
        String code = appleService.updatePurchaseHistory(userReceiptRequest);

        return ResponseEntity.ok(code);
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseBodyV2DecodedPayload> test() throws InterruptedException {
        SendTestNotificationResponse sendTestNotificationResponse = appleService.requestTestNotification();
        Thread.sleep(1000 * 5);
        CheckTestNotificationResponse checkResponse = appleService.checkTestNotification(sendTestNotificationResponse);
        return ResponseEntity.ok(appleService.signedPayloadNotification(checkResponse.getSignedPayload()));
    }

    @PostMapping("/test")
    public ResponseEntity<List<String>> transactions(
            @RequestBody @Valid UserReceiptRequest userReceiptRequest) {
        List<String> transactions = appleService.getTransactionHistory(userReceiptRequest);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/status")
    public ResponseEntity<ResponseBodyV2DecodedPayload> notificationStatus(
            @RequestBody ResponseBodyV2 responseBodyV2) {

        ResponseBodyV2DecodedPayload notificationPayload = appleService.getNotificationPayload(responseBodyV2);
        return ResponseEntity.ok(notificationPayload);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<String> transactions(
            @PathVariable String transactionId) {
        String transactionInfo = appleService.getTransactionInfo(transactionId);
        return ResponseEntity.ok(transactionInfo);
    }


}
