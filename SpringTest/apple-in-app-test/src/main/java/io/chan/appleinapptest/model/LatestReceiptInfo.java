package io.chan.appleinapptest.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * Apple Document URL ‣ https://developer.apple.com/documentation/appstorereceipts/responsebody/receipt/in_app
 */
@Getter
public class LatestReceiptInfo {

    @JsonAlias("cancellation_date")
    private String cancellationDate;
    @JsonAlias("cancellation_date_ms")
    private String cancellationDateMs;
    @JsonAlias("cancellation_date_pst")
    private String cancellationDatePst;
    @JsonAlias("cancellation_reason")
    private String cancellationReason;
    @JsonAlias("expires_date")
    private String expiresDate;
    @JsonAlias("expires_date_ms")
    private String expiresDateMs;
    @JsonAlias("expires_date_pst")
    private String expiresDatePst;
    @JsonAlias("is_in_intro_offer_period")
    private String isInIntroOfferPeriod;
    @JsonAlias("is_trial_period")
    private String isTrialPeriod;
    @JsonAlias("is_upgraded")
    private String isUpgraded;
    @JsonAlias("offer_code_ref_name")
    private String offerCodeRefName;
    @JsonAlias("original_purchase_date")
    private String originalPurchaseDate;
    @JsonAlias("original_purchase_date_ms")
    private String originalPurchaseDateMs;
    @JsonAlias("original_purchase_date_pst")
    private String originalPurchaseDatePst;
    @JsonAlias("original_transaction_id")
    private String originalTransactionId;
    @JsonAlias("product_id")
    private String productId;
    @JsonAlias("promotional_offer_id")
    private String promotionalOfferId;
    @JsonAlias("purchase_date")
    private String purchaseDate;
    @JsonAlias("purchase_date_ms")
    private String purchaseDateMs;
    @JsonAlias("purchase_date_pst")
    private String purchaseDatePst;
    private String quantity;
    @JsonAlias("subscription_group_identifier")
    private String subscriptionGroupIdentifier;
    @JsonAlias("transaction_id")
    private String transactionId;
    @JsonAlias("web_order_line_item_id")
    private String webOrderLineItemId;

}
