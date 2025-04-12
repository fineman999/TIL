package io.chan.queuingsystemforjava.common.error;

import java.util.function.Supplier;

public class IgnoreException extends RuntimeException implements Supplier<String> {
    private final ErrorCode errorCode;

    public IgnoreException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public IgnoreException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String get() {
        return errorCode.getMessage();
    }
}
