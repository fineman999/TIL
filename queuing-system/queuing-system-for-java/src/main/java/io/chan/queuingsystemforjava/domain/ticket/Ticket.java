package io.chan.queuingsystemforjava.domain.ticket;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    private UUID ticketSerialNumber;

    public static Ticket create(final Member member, final Seat seat, final String paymentKey, final BigDecimal amount) {
        return Ticket.builder()
                .member(member)
                .seat(seat)
                .paymentKey(paymentKey)
                .amount(amount)
                .status(TicketStatus.ISSUED)
                .ticketSerialNumber(UUID.randomUUID())
                .build();
    }

    public void markAsUsed() {
        if (!status.isIssued()) {
            throw new TicketingException(ErrorCode.INVALID_TICKET_STATUS);
        }
        this.status = TicketStatus.USED;
    }
    public void markAsCancelled() {
        if (!status.isIssued()) {
            throw new TicketingException(ErrorCode.INVALID_TICKET_STATUS);
        }
        this.status = TicketStatus.CANCELLED;
    }
}
