package io.chan.queuingsystemforjava.domain.seat;

public enum SeatStatus {
    SELECTABLE,
    SELECTED,
    PENDING_PAYMENT,
    PAID;

    public boolean isSelectable() {
        return this == SELECTABLE;
    }

    public boolean isNotSelected() {
        return this != SELECTED;
    }

    public boolean isPendingPayment() {
        return this == PENDING_PAYMENT;
    }

    public boolean isPaid() {
        return this == PAID;
    }
}
