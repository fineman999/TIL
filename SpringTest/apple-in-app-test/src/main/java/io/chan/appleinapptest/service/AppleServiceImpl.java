package io.chan.appleinapptest.service;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.migration.ReceiptUtility;
import com.apple.itunes.storekit.model.*;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import io.chan.appleinapptest.UserReceiptRequest;
import io.chan.appleinapptest.model.AppStoreResponse;
import io.chan.appleinapptest.model.InApp;
import io.chan.appleinapptest.model.UserReceiptInfo;
import io.chan.appleinapptest.utils.AppleUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {
    private final Logger logger = LoggerFactory.getLogger(AppleServiceImpl.class);

    private final AppleUtils appleUtils;
    private final AppStoreServerAPIClient appStoreServerAPIClient;
    private final SignedDataVerifier signedDataVerifier;

    @Override
    public String updatePurchaseHistory(UserReceiptRequest userReceiptRequest) {
        AppStoreResponse appStoreResponse = appleUtils.verifyReceipt(userReceiptRequest);

        UserReceiptInfo userReceiptInfo = null;
        if (appStoreResponse.getReceipt().getInApp().size() == 1) { // 소모품 (Consumable)
            userReceiptInfo = getUserReceiptInfo(appStoreResponse.getReceipt().getInApp().get(0));
        } else {
            // TODO - 비소모품(Non-Consumable), 자동 갱신 구독(Auto-Renewable Subscription), 비자동 갱신 구독(Non-Renewable Subscription)에 대한 로직 필요.
        }

        logger.info("===== DATABASE INSERT IGNORE =====");
        logger.info("product_id ‣ " + userReceiptInfo.getProductId());
        logger.info("transaction_id ‣ " + userReceiptInfo.getTransactionId());
        logger.info("original_transaction_id ‣ " + userReceiptInfo.getOriginalTransactionId());
        logger.info("purchase_data_ms ‣ " + userReceiptInfo.getPurchaseDateMs());
        logger.info("expiration_date ‣ " + userReceiptInfo.getExpirationDate());
        logger.info("====================================");

        return "Success";
    }

    /**
     *
     * GET /inApps/v1/notification/test
     * @return
     */
    @Override
    public SendTestNotificationResponse requestTestNotification() {
        try {
            return appStoreServerAPIClient.requestTestNotification();
        } catch (APIException e) {
            logger.error("APIException", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /inApps/v1/notification/history
     *
     * @param paginationToken
     * @return Response
     * {
     * "notificationHistory": [
     * {
     * "signedPayload": "string",
     * "sendAttempts": [
     * {
     * "attemptDate": "1629390000000",
     * "sendAttemptResult": "NO_RESPONSE",
     * },
     * {
     * "attemptDate": "1629390000000",
     * "sendAttemptResult": "TIMED_OUT",
     * }
     * ]
     * }
     * ],
     * ...}
     */
    @Override
    public NotificationHistoryResponse getNotificationHistory(String paginationToken) {
        try {
            NotificationHistoryRequest notificationHistoryRequest = new NotificationHistoryRequest();
            notificationHistoryRequest.setOnlyFailures(true); // 실패한 알림만 가져옵니다.
            return appStoreServerAPIClient.getNotificationHistory(paginationToken, notificationHistoryRequest);
        } catch (APIException e) {
            logger.error("APIException", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /inApps/v1/notification/test/{test_notification_token}
     * @param sendTestNotificationResponse
     * @return
     */
    @Override
    public CheckTestNotificationResponse checkTestNotification(SendTestNotificationResponse sendTestNotificationResponse) {
        try {
            return appStoreServerAPIClient.getTestNotificationStatus(sendTestNotificationResponse.getTestNotificationToken());
        } catch (APIException e) {
            logger.error("APIException", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseBodyV2DecodedPayload signedPayloadNotification(String signedData) {
        try {
            return signedDataVerifier.verifyAndDecodeNotification(signedData);
        } catch (VerificationException e) {
            logger.error("VerificationException", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * get Transaction Info Response
     * @param signedData
     * @return JWSTransactionDecodedPayload
     *
     * Response
     * {
     * "transactionId": "1000000782476479",
     * "originalTransactionId": "1000000782476479",
     * "type": "Consumable",
     * "environment": "Sandbox" // or "Production"
     * "quantity": 1,
     * "bundleId": "com.witch.witch",
     * "purchaseDate": "1629390000000",
     * "originalPurchaseDate": "1629390000000",
     * "inAppOwnershipType": "PURCHASED",
     * "signedDate": "1629390000000",
     * "appAccountToken": "37e4f4f0-1b1b-4b1b-8b1b-9b1b1b1b1b1b",
     * "storefront": "KR",
     * "storefrontId": "143466",
     * "transactionReason": "PURCHASE",
     *
     */
    @Override
    public JWSTransactionDecodedPayload signedPayloadTransaction(String signedData) {
        try {
            return signedDataVerifier.verifyAndDecodeTransaction(signedData);
        } catch (VerificationException e) {
            logger.error("VerificationException", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * get transaction info
     * Request
     * GET inApps/v1/transactions/{transaction_id}
     * <p>
     * Response
     * {
     * "signedTransactionInfo": "string"
     * }
     */
    @Override
    public String getTransactionInfo(String transactionId) {
        try {
            TransactionInfoResponse transactionInfo = appStoreServerAPIClient.getTransactionInfo(transactionId);
            return transactionInfo.getSignedTransactionInfo();
        } catch (APIException | IOException e) {
            logger.error("APIException | IOException", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Get Transaction History
     * GET inApps/v1/history/{transaction_id}
     * @param userReceiptRequest
     *  TransactionHistoryRequest를 통해 시작시간, 끝나는 시간, 정렬 방법을 설정할 수 있습니다.
     *  그 후 getTransactionHistory를 통해 TransactionHistoryRequest에 해당되는 트랜잭션을 가져옵니다.
     */
    @Override
    public List<String> getTransactionHistory(UserReceiptRequest userReceiptRequest) {
        String appReceipt = userReceiptRequest.receiptData();
        ReceiptUtility receiptUtil = new ReceiptUtility();
        try {
            String transactionId = receiptUtil.extractTransactionIdFromAppReceipt(appReceipt);
            TransactionHistoryRequest request = new TransactionHistoryRequest()
                    .sort(TransactionHistoryRequest.Order.ASCENDING)
                    .revoked(false)
                    .productTypes(List.of(TransactionHistoryRequest.ProductType.CONSUMABLE));
            HistoryResponse response = null;
            List<String> transactions = new LinkedList<>();
            do {
                String revision = response != null ? response.getRevision() : null;
                try {
                    response = appStoreServerAPIClient.getTransactionHistory(transactionId, revision, request);
                } catch (APIException | IOException e) {
                    logger.error("APIException | IOException", e);
                    throw new RuntimeException(e);
                }
                transactions.addAll(response.getSignedTransactions());
            } while (response.getHasMore());
            return transactions;
        } catch (IOException e) {
            logger.error("IOException", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseBodyV2DecodedPayload getNotificationPayload(ResponseBodyV2 responseBodyV2) {
        try {
            ResponseBodyV2DecodedPayload responseBodyV2DecodedPayload = signedDataVerifier.verifyAndDecodeNotification(responseBodyV2.getSignedPayload());
            return responseBodyV2DecodedPayload;
        } catch (VerificationException e) {
            logger.error("VerificationException", e);
            throw new RuntimeException(e);
        }
    }

    private UserReceiptInfo getUserReceiptInfo(InApp inApp) {

        // TODO - 상품(product_id)에 대한 기간을 가져온 후 구매 날짜(purchase_data_ms)를 기준으로 만료 날짜(expiration_data)를 계산하는 로직 필요.

        return new UserReceiptInfo.Builder(inApp.getProductId(), inApp.getTransactionId(), inApp.getOriginalTransactionId())
                .purchaseDateMs(inApp.getPurchaseDateMs())
                .expirationDate("9999-12-31") // 만료 날짜
                .build();
    }
}
