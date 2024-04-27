package io.chan.resilience4jpractice.exception;

public class IgnoreException extends RuntimeException {

    public IgnoreException(String message) {
        super(message);
    }
}