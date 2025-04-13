package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.PaymentEvent;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.SeatEvent;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import io.chan.queuingsystemforjava.domain.ticket.strategy.LockSeatStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * 예약 트랜잭션 서비스
 * <p>
 *     1. 좌석 선택
 *     2. 좌석 해제
 *     3. 티켓 예약
 */
@Slf4j
@RequiredArgsConstructor
public class ReservationTransactionService implements ReservationService {
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final SeatGradeRepository seatGradeRepository;
    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orderRepository;
    private final LockSeatStrategy lockSeatStrategy;
    private final EventPublisher eventPublisher;
    private final ReservationManager reservationManager;
    private final int reservationReleaseDelay;

    private final TaskScheduler scheduler;

    @Override
    @Transactional
    public void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        Long seatId = seatSelectionRequest.seatId();

        Seat seat =
                lockSeatStrategy
                        .getSeatWithLock(seatId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT));

        Member member =
                memberRepository
                        .findByEmail(memberEmail)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_MEMBER));

        seat.assignByMember(member);

        // 좌석 선택 이벤트 발행
        eventPublisher.publish(new SeatEvent(memberEmail, seatId, SeatEvent.EventType.SELECT));

        // Instant를 사용해 reservationReleaseDelay 초 후 실행
        Instant releaseTime = Instant.now().plusSeconds(reservationReleaseDelay);
        scheduler.schedule(
                () -> reservationManager.releaseSeat(member, seatId),
                releaseTime
        );
    }

    @Override
    @Transactional
    public void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        Member member =
                memberRepository
                        .findByEmail(memberEmail)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_MEMBER));

        Long seatId = seatSelectionRequest.seatId();
        reservationManager.releaseSeat(member, seatId);

        // 좌석 해제 이벤트 발행
        eventPublisher.publish(new SeatEvent(memberEmail, seatId, SeatEvent.EventType.RELEASE));
    }

    @Override
    @Transactional
    public TicketPaymentResponse reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest) {
        Long seatId = ticketPaymentRequest.seatId();
        Seat seat =
                lockSeatStrategy
                        .getSeatWithLock(seatId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT));

        Member loginMember =
                memberRepository
                        .findByEmail(memberEmail)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_MEMBER));

        // TODO: toss 결제 API 연동
        processPayment(seat, loginMember);

        SeatGrade seatGrade =
                seatGradeRepository
                        .findById(seat.getSeatGradeId())
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT_GRADE));
        if (seatGrade.isNotMatchAmount(ticketPaymentRequest.amount())) {
            throw new TicketingException(ErrorCode.NOT_MATCH_AMOUNT);
        }

        Order order =
                orderRepository
                        .findByOrderId(ticketPaymentRequest.orderId())
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));

        order.markAsCompleted();

        Ticket ticket = Ticket.create(
                loginMember,
                seat,
                ticketPaymentRequest.paymentKey(),
                seatGrade.getPrice(),
                order
        );

        ticket = ticketRepository.save(ticket);

        return TicketPaymentResponse.create(ticket.getTicketId());
    }

    private void processPayment(Seat seat, Member loginMember) {
        if (seat.isNotAssignedByMember(loginMember)) {
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT);
        }
        seat.checkSeatStatusPendingPayment(loginMember);
        paymentProcessor.processPayment(new PaymentRequest("paymentId", seat.getSeatCode(),0L));
        seat.markAsPaid();
        PaymentEvent paymentEvent = new PaymentEvent(loginMember.getEmail());
        eventPublisher.publish(paymentEvent);
    }
}