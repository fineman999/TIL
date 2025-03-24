package io.chan.queuingsystemforjava.domain.ticket.repository;

import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
}
