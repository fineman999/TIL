package io.chan.resilience4jpractice.exception;

public class RetryException extends RuntimeException {
    public RetryException(String message) {
        super(message);
    }
}