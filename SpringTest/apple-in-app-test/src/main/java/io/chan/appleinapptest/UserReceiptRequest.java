package io.chan.appleinapptest;

import jakarta.validation.constraints.NotNull;

public record UserReceiptRequest(
        @NotNull
        String receiptData
) {
}
