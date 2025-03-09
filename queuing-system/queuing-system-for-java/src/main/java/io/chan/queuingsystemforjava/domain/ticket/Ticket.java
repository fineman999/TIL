package io.chan.queuingsystemforjava.domain.ticket;

import io.chan.queuingsystemforjava.common.BaseEntity;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE tickets SET deleted_at = NOW() WHERE ticket_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private UUID ticketSerialNumber;
}
