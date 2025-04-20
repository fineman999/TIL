package io.chan.queuingsystemforjava.domain.ticket.dto.response;

public record TicketCancelResponse(
    String paymentKey,
    String lastTransactionKey
) {}