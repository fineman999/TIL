package io.chan.queuingsystemforjava.common.error;

import lombok.Getter;

import java.io.IOException;
import java.util.function.Supplier;

@Getter
public class TicketingException extends RuntimeException implements Supplier<String> {
    private final ErrorCode errorCode;

    public TicketingException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public TicketingException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TicketingException(ErrorCode errorCode, String message, Exception e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public TicketingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String get() {
        return errorCode.getMessage();
    }
}
