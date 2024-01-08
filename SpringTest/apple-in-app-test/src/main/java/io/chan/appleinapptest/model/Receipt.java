package io.chan.appleinapptest.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Apple Document URL ‣ https://developer.apple.com/documentation/appstorereceipts/responsebody/receipt
 */
@Getter
@Setter
public class Receipt {

    @JsonAlias("adam_id")
    private Number adamId;
    @JsonAlias("app_item_id")
    private Number app_item_id;
    @JsonAlias("application_version")
    private String applicationVersion;
    @JsonAlias("bundle_id")
    private String bundleId;
    @JsonAlias("download_id")
    private Integer downloadId;
    @JsonAlias("expiration_date")
    private String expirationDate;
    @JsonAlias("expiration_date_ms")
    private String expirationDateMs;
    @JsonAlias("expiration_date_pst")
    private String expirationDatePst;
    @JsonAlias("in_app")
    private List<InApp> inApp;
    @JsonAlias("original_application_version")
    private String originalApplicationVersion;
    @JsonAlias("original_purchase_date")
    private String originalPurchaseDate;
    @JsonAlias("original_purchase_date_ms")
    private String originalPurchaseDateMs;
    @JsonAlias("original_purchase_date_pst")
    private String originalPurchaseDatePst;
    @JsonAlias("preorder_date")
    private String preorderDate;
    @JsonAlias("preorder_date_ms")
    private String preorderDateMs;
    @JsonAlias("preorder_date_pst")
    private String preorderDatePst;
    @JsonAlias("receipt_creation_date")
    private String receiptCreationDate;
    @JsonAlias("receipt_creation_date_ms")
    private String receiptCreationDateMs;
    @JsonAlias("receipt_creation_date_pst")
    private String receiptCreationDatePst;
    @JsonAlias("receipt_type")
    private String receiptType;
    @JsonAlias("request_date")
    private String requestDate;
    @JsonAlias("request_date_ms")
    private String requestDateMs;
    @JsonAlias("request_date_pst")
    private String requestDatePst;
    @JsonAlias("version_external_identifier")
    private Integer versionExternalIdentifier;

}