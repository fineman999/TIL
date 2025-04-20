package io.chan.queuingsystemforjava.domain.ticket.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TicketRepository {
    private final TicketJpaRepository ticketJpaRepository;

    public Ticket save(Ticket ticket) {
        return ticketJpaRepository.save(ticket);
    }

    public List<Ticket> findAllByMember(Member member) {
        return ticketJpaRepository.findAllByMember(member);
    }

    public Optional<Ticket> findById(final Long id) {
        return ticketJpaRepository.findById(id);
    }

    public Optional<Ticket> findByIdWithOrder(final Long ticketId) {
        return ticketJpaRepository.findByTicketIdWithOrder(ticketId);
    }
}
