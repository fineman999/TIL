package io.chan.resilience4jpractice.exception;

public class SomeException extends RuntimeException {
    public SomeException(String message) {
        super(message);
    }
}