package io.chan.queuingsystemforjava.domain.ticket.proxy;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationTransactionService;
import jakarta.persistence.LockTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;

@Slf4j
@RequiredArgsConstructor
public class PessimisticReservationServiceProxy implements ReservationServiceProxy {
    private final ReservationTransactionService reservationTransactionService;

    @Override
    public void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        try {
            reservationTransactionService.selectSeat(memberEmail, seatSelectionRequest);
        } catch (PessimisticLockingFailureException | LockTimeoutException e) {
            log.error(e.getMessage(), e);
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT);
        }
    }

    @Override
    public TicketPaymentResponse reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest) {
        try {
            return reservationTransactionService.reservationTicket(memberEmail, ticketPaymentRequest);
        } catch (PessimisticLockingFailureException | LockTimeoutException e) {
            log.error(e.getMessage(), e);
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT);
        }
    }

    @Override
    public void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        reservationTransactionService.releaseSeat(memberEmail, seatSelectionRequest);
    }
}