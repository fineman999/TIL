package io.chan.appleinapptest.service;

import com.apple.itunes.storekit.model.*;
import io.chan.appleinapptest.UserReceiptRequest;

import java.util.List;

public interface AppleService {
    String updatePurchaseHistory(UserReceiptRequest userReceiptRequest);
    SendTestNotificationResponse requestTestNotification();

    NotificationHistoryResponse getNotificationHistory(String paginationToken);

    CheckTestNotificationResponse checkTestNotification(SendTestNotificationResponse sendTestNotificationResponse);

    ResponseBodyV2DecodedPayload signedPayloadNotification(String signedData);

    JWSTransactionDecodedPayload signedPayloadTransaction(String signedData);

    String getTransactionInfo(String transactionId);

    List<String> getTransactionHistory(UserReceiptRequest userReceiptRequest);

    ResponseBodyV2DecodedPayload getNotificationPayload(ResponseBodyV2 responseBodyV2);
}
