package io.chan.appleinapptest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.appleinapptest.UserReceiptRequest;
import io.chan.appleinapptest.model.AppStoreResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class AppleUtils {

    private final Logger logger = LoggerFactory.getLogger(AppleUtils.class);

    @Value("${apple.production-url}")
    private String appleProductionUrl;

    @Value("${apple.sandbox-url}")
    private String appleSandboxUrl;

    @Value("${apple.issuer-id}")
    private String issuerId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.bundle-id}")
    private String bundleId;

    @Value("${apple.apple-id}")
    private Long appleId;

    /**
     * App에서 전달받은 receipt-data를 Apple에 유효성 확인 요청
     * Apple Document URL ‣ https://developer.apple.com/documentation/appstorereceipts/verifyreceipt
     *
     * @param userReceipt
     * @throws IllegalStateException
     */
    public AppStoreResponse verifyReceipt(UserReceiptRequest userReceipt) throws IllegalStateException {

        Map<String, String> appStoreRequest = new HashMap<>();
        appStoreRequest.put("receipt-data", userReceipt.receiptData());

        AppStoreResponse appStoreResponse = WebClientUtils.doPost(appleProductionUrl, appStoreRequest);


        int statusCode = appStoreResponse.getStatus();
        if (statusCode == 21007) {
            appStoreResponse = WebClientUtils.doPost(appleSandboxUrl, appStoreRequest);
        } else if (statusCode != 0) {
            verifyStatusCode(statusCode);
        }

        return appStoreResponse;
    }

    private void verifyStatusCode(int statusCode) {
        switch (statusCode) {
            case 21000:
                logger.error("[Status code: " + statusCode + "] The request to the App Store was not made using the HTTP POST request method.");
                break;
            case 21001:
                logger.error("[Status code: " + statusCode + "] This status code is no longer sent by the App Store.");
                break;
            case 21002:
                logger.error("[Status code: " + statusCode + "] The data in the receipt-data property was malformed or the service experienced a temporary issue. Try again.");
                break;
            case 21003:
                logger.error("[Status code: " + statusCode + "] The receipt could not be authenticated.");
                break;
            case 21004:
                logger.error("[Status code: " + statusCode + "] The shared secret you provided does not match the shared secret on file for your account.");
                break;
            case 21005:
                logger.error("[Status code: " + statusCode + "] The receipt server was temporarily unable to provide the receipt. Try again.");
                break;
            case 21006:
                logger.error("[Status code: " + statusCode + "] This receipt is valid but the subscription has expired. When this status code is returned to your server, the receipt data is also decoded and returned as part of the response. Only returned for iOS 6-style transaction receipts for auto-renewable subscriptions.");
                break;
            case 21008:
                logger.error("[Status code: " + statusCode + "] This receipt is from the production environment, but it was sent to the test environment for verification.");
                break;
            case 21009:
                logger.error("[Status code: " + statusCode + "] Internal data access error. Try again later.");
                break;
            case 21010:
                logger.error("[Status code: " + statusCode + "] The user account cannot be found or has been deleted.");
                break;
            default:
                logger.error("[Status code: " + statusCode + "] The receipt for the App Store is incorrect.");
                break;
        }

        throw new IllegalStateException("[/verifyReceipt] The receipt for the App Store is incorrect.");
    }

}

