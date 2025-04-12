package io.chan.queuingsystemforjava.domain.ticket;

public enum TicketStatus {
    ISSUED,
    USED,
    CANCELLED;

    public boolean isIssued() {
        return this == ISSUED;
    }

    public boolean isUsed() {
        return this == USED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
