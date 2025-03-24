package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;

public interface ReservationService {
    void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest);

    void reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest);

    void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest);
}