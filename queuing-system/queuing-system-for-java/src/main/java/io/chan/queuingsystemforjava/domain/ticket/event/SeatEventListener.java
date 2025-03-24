
package io.chan.queuingsystemforjava.domain.ticket.event;

import io.chan.queuingsystemforjava.domain.ticket.controller.TicketSseController;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.SeatEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SeatEventListener {
    private final TicketSseController ticketSseController;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSeatEvent(SeatEvent event) {
        ticketSseController.sendEventToPerformance(event);
    }
}