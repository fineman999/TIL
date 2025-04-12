package io.chan.queuingsystemforjava.domain.seat;


import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "seats")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@SQLDelete(sql = "UPDATE seats SET deleted_at = NOW() WHERE seat_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_grade_id", nullable = false)
    private SeatGrade seatGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 32, nullable = false)
    private String seatCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 16, nullable = false)
    private SeatStatus seatStatus = SeatStatus.SELECTABLE;

    @Version private Long version;

    public boolean isSelectable() {
        return seatStatus.isSelectable();
    }

    public void assignByMember(Member member) {
        if (!isSelectable()) {
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT);
        }
        log.info("seat occupied by {}", member.getEmail());
        this.member = member;
        this.seatStatus = SeatStatus.SELECTED;
    }

    public void markAsPendingPayment() {
        if (seatStatus.isNotSelected()) {
            throw new TicketingException(ErrorCode.INVALID_SEAT_STATUS);
        }
        this.seatStatus = SeatStatus.PENDING_PAYMENT;
    }

    public void markAsPaid() {
        if (!seatStatus.isPendingPayment()) {
            throw new TicketingException(ErrorCode.INVALID_SEAT_STATUS);
        }
        this.seatStatus = SeatStatus.PAID;
    }

    public boolean isNotAssignedByMember(Member loginMember) {
        return !loginMember.getMemberId().equals(member.getMemberId());
    }

    public void releaseSeat(Member loginMember) {
        if (seatStatus.isNotSelected() || isNotAssignedByMember(loginMember)) {
            return;
        }
        this.seatStatus = SeatStatus.SELECTABLE;
    }

    public void checkSeatStatusSelected(final Member member) {
        if (seatStatus.isNotSelected() || isNotAssignedByMember(member)) {
            throw new TicketingException(ErrorCode.INVALID_SEAT_STATUS);
        }
    }

    public void checkSeatStatusPendingPayment(final Member loginMember) {
        if (!seatStatus.isPendingPayment()) {
            throw new TicketingException(ErrorCode.INVALID_SEAT_STATUS);
        }
        if (isNotAssignedByMember(loginMember)) {
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT);
        }
    }
}
