package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketCancelRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketCancelResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationService {
    void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest);

    TicketPaymentResponse reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest);

    void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest);
}