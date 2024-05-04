package io.chan.productservice.exception;

public class NamedLockException extends RuntimeException {
    public NamedLockException(String message) {
        super(message);
    }
}
