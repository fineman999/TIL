package io.chan.queuingsystemforjava.domain.ticket.dto;

public record TicketPaymentResponse(
        String message,
        Long ticketId
) {
    public static TicketPaymentResponse create(final Long ticketId) {
        return new TicketPaymentResponse("티켓 결제가 완료되었습니다.", ticketId);
    }

    public static TicketPaymentResponse failure() {
        return new TicketPaymentResponse("티켓 결제에 실패했습니다.", null);
    }
}
