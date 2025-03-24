package io.chan.queuingsystemforjava.domain.ticket.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepository {
    private final TicketJpaRepository ticketJpaRepository;

    public void save(Ticket ticket) {
        ticketJpaRepository.save(ticket);
    }

    public List<Ticket> findAllByMember(Member member) {
        return ticketJpaRepository.findAllByMember(member);
    }
}
