package io.chan.queuingsystemforjava.common.error;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class PaymentException extends RuntimeException implements Supplier<String> {
    private final ErrorCode errorCode;

    public PaymentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PaymentException(ErrorCode errorCode, String message, Exception e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public PaymentException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String get() {
        return errorCode.getMessage();
    }
}
