package io.chan.springbatch.session11.retry;

public class RetryableException extends Exception {
    public RetryableException(String s) {
        super(s);
    }
}
