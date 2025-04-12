package io.chan.queuingsystemforjava.domain.order;

public enum OrderStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;

    public boolean isPending() {
        return this == PENDING;
    }
}