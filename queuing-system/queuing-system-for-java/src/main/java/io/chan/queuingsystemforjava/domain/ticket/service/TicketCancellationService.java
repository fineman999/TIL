package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelRequest;
import io.chan.queuingsystemforjava.domain.payment.dto.PaymentCancelResponse;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCancellationService;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import io.chan.queuingsystemforjava.domain.ticket.dto.TicketPaymentResponse;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.SeatEvent;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketCancelRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketCancelResponse;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketCancellationService {
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final PaymentCancellationService paymentCancellationService;
    private final IdempotencyRedisRepository idempotencyRedisRepository;
    private final EventPublisher eventPublisher;

    // TODO: 현재 결제 취소 진행중,,,(seat 동시성 보장을 위해 seat 락 걸어서 호출하기)
    @Transactional
    public TicketCancelResponse cancelTicket(Member member, TicketCancelRequest cancelRequest) {
        Ticket ticket = ticketRepository.findByIdWithOrder(cancelRequest.ticketId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_TICKET));

        if (!ticket.isOwnedBy(member)) {
            throw new TicketingException(ErrorCode.UNAUTHORIZED_ACCESS, "Ticket does not belong to this member");
        }

        Order order = orderRepository.findByOrderId(ticket.getOrder().getOrderId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));

        PaymentCancelRequest paymentCancelRequest = new PaymentCancelRequest(
                cancelRequest.cancelReason(),
                cancelRequest.cancelAmount(),
                cancelRequest.refundReceiveAccount() != null ? new PaymentCancelRequest.RefundReceiveAccount(
                        cancelRequest.refundReceiveAccount().bank(),
                        cancelRequest.refundReceiveAccount().accountNumber(),
                        cancelRequest.refundReceiveAccount().holderName()
                ) : null,
                cancelRequest.taxFreeAmount(),
                "KRW"
        );

        String idempotencyKey = idempotencyRedisRepository.generateIdempotencyKey();

        PaymentCancelResponse cancelResponse = paymentCancellationService.cancelPayment(
                cancelRequest.paymentKey(),
                paymentCancelRequest,
                idempotencyKey
        );

        ticket.markAsCancelled();
        Seat seat = ticket.getSeat();
        seat.releaseSeat(member);
        order.markAsCancelled();

        eventPublisher.publish(new SeatEvent(member.getEmail(), seat.getSeatId(), SeatEvent.EventType.RELEASE));

        return new TicketCancelResponse(cancelResponse.paymentKey(), cancelResponse.lastTransactionKey());
    }

}
