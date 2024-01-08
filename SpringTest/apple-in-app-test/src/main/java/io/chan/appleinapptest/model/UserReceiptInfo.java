package io.chan.appleinapptest.model;

import lombok.Getter;

@Getter
public class UserReceiptInfo {

    // TODO - User(구매자) 정보에 대한 Fields 필요.

    private final String productId;
    private final String transactionId;
    private final String originalTransactionId;
    private final String purchaseDateMs;
    private final String expirationDate;

    public static class Builder {
        private final String productId;
        private final String transactionId;
        private final String originalTransactionId;

        private String purchaseDateMs;
        private String expirationDate;

        public Builder(String productId, String transactionId, String originalTransactionId) {
            this.productId = productId;
            this.transactionId = transactionId;
            this.originalTransactionId = originalTransactionId;
        }

        public Builder purchaseDateMs(String val) {
            purchaseDateMs = val;
            return this;
        }

        public Builder expirationDate(String val) {
            expirationDate = val;
            return this;
        }

        public UserReceiptInfo build() {
            return new UserReceiptInfo(this);
        }
    }

    private UserReceiptInfo(Builder builder) {
        productId = builder.productId;
        transactionId = builder.transactionId;
        originalTransactionId = builder.originalTransactionId;
        purchaseDateMs = builder.purchaseDateMs;
        expirationDate = builder.expirationDate;
    }
}
