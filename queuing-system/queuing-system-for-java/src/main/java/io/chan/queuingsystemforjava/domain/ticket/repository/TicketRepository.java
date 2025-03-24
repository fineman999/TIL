package io.chan.queuingsystemforjava.domain.ticket.repository;

import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepository {
    private final TicketJpaRepository ticketJpaRepository;

    public void save(Ticket ticket) {
        ticketJpaRepository.save(ticket);
    }
}
