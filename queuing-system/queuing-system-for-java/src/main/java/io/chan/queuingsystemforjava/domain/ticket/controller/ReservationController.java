package io.chan.queuingsystemforjava.domain.ticket.controller;

import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationService;
import io.chan.queuingsystemforjava.domain.waitingsystem.aop.Waiting;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(@Qualifier("persistencePessimisticReservationService") ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PostMapping("/seats/release")
    public ResponseEntity<Void> releaseSeat(
            @LoginMember MemberContext memberContext,
            @RequestBody @Valid SeatSelectionRequest seatSelectionRequest) {
        String memberEmail = memberContext.getUsername();
        reservationService.releaseSeat(memberEmail, seatSelectionRequest);
        return ResponseEntity.ok().build();
    }

    @Waiting
    @PostMapping("/seats/select")
    public ResponseEntity<Void> selectSeat(
            @LoginMember MemberContext memberContext,
            @RequestBody @Valid SeatSelectionRequest seatSelectionRequest) {
        String memberEmail = memberContext.getUsername();
        reservationService.selectSeat(memberEmail, seatSelectionRequest);
        return ResponseEntity.ok().build();
    }

    @Waiting
    @PostMapping("/tickets")
    public ResponseEntity<Void> reservationTicket(
            @LoginMember MemberContext memberContext,
            @RequestBody @Valid TicketPaymentRequest ticketPaymentRequest) {
        String memberEmail = memberContext.getUsername();
        reservationService.reservationTicket(memberEmail, ticketPaymentRequest);
        return ResponseEntity.ok().build();
    }
}