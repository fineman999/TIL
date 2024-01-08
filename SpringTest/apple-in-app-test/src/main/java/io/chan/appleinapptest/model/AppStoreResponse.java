package io.chan.appleinapptest.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Apple Document URL ‣ https://developer.apple.com/documentation/appstorereceipts/responsebody/receipt/in_app
 */
@Getter
@Setter
public class AppStoreResponse {

    private String environment;
    @JsonAlias("is-retryable")
    private boolean isRetryable;
    @JsonAlias("latest_receipt")
    private String latestReceipt;
    @JsonAlias("latest_receipt_info")
    private List<LatestReceiptInfo> latestReceiptInfo;
    @JsonAlias("pending_renewal_info")
    private List<PendingRenewalInfo> pendingRenewalInfo;
    private Receipt receipt;
    private int status;

}

