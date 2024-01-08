package io.chan.appleinapptest.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * Apple Document URL ‣ https://developer.apple.com/documentation/appstorereceipts/responsebody/pending_renewal_info
 */
@Setter
@Getter
public class PendingRenewalInfo {

    @JsonAlias("auto_renew_product_id")
    private String autoRenewProductId;
    @JsonAlias("auto_renew_status")
    private String autoRenewStatus;
    @JsonAlias("expiration_intent")
    private String expirationIntent;
    @JsonAlias("grace_period_expires_date")
    private String gracePeriodExpiresDate;
    @JsonAlias("grace_period_expires_date_ms")
    private String gracePeriodExpiresDateMs;
    @JsonAlias("grace_period_expires_date_pst")
    private String gracePeriodExpiresDatePst;
    @JsonAlias("is_in_billing_retry_period")
    private String isInBillingRetryPeriod;
    @JsonAlias("offer_code_ref_name")
    private String offerCodeRefName;
    @JsonAlias("original_transaction_id")
    private String originalTransactionId;
    @JsonAlias("price_consent_status")
    private String priceConsentStatus;
    @JsonAlias("product_id")
    private String productId;

}