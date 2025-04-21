package io.chan.queuingsystemforjava.domain.ticket.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
    @Query(
            """
                    SELECT t FROM Ticket t
                                JOIN FETCH t.member m
                                JOIN FETCH t.seat s
                                JOIN FETCH s.seatGrade sg
                                JOIN FETCH s.zone z
                                JOIN FETCH z.performance p
                                WHERE t.member = :member
                    """)
    List<Ticket> findAllByMember(Member member);

    @Query(
            """
                    SELECT t FROM Ticket t
                        JOIN FETCH t.order o
                        WHERE t.ticketId = :ticketId
                    """)
    Optional<Ticket> findByTicketIdWithOrder(Long ticketId);

    @Query("SELECT t FROM Ticket as t WHERE t.ticketId = :ticketId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Ticket> findByTicketIdWithPessimistic(@Param("ticketId") Long ticketId);
}
