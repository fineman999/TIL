package io.chan.resilience4jpractice.exception;

public class RecordException extends RuntimeException {
    public RecordException(String message) {
        super(message);
    }
};