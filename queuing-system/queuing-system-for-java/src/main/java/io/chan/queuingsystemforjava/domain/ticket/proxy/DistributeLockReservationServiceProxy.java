package io.chan.queuingsystemforjava.domain.ticket.proxy;

import io.chan.queuingsystemforjava.domain.ticket.aop.DistributedLock;
import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DistributeLockReservationServiceProxy implements ReservationServiceProxy{
    private final ReservationTransactionService reservationTransactionService;

    @DistributedLock(key = "'seatId-' + #seatSelectionRequest.seatId()", waitTime = 5, leaseTime = 10)
    @Override
    public void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        reservationTransactionService.selectSeat(memberEmail, seatSelectionRequest);
    }

    @Override
    public TicketPaymentResponse reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest) {
        return reservationTransactionService.reservationTicket(memberEmail, ticketPaymentRequest);
    }

    @DistributedLock(key = "'seatId-' + #seatSelectionRequest.seatId()", waitTime = 5, leaseTime = 10)
    @Override
    public void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        reservationTransactionService.releaseSeat(memberEmail, seatSelectionRequest);
    }
}
