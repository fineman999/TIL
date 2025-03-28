package io.chan.queuingsystemforjava.domain.ticket.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketElement;
import io.chan.queuingsystemforjava.domain.ticket.service.TicketService;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/members/tickets")
    public ResponseEntity<ItemResult<TicketElement>> selectMyTickets(
            @LoginMember MemberContext memberContext) {
        String memberEmail = memberContext.getUsername();
        ItemResult<TicketElement> tickets = ticketService.selectMyTicket(memberEmail);
        return ResponseEntity.ok().body(tickets);
    }
}
