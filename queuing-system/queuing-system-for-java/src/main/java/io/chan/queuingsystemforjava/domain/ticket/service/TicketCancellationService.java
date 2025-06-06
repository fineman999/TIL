package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.Payment;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.repository.PaymentJpaRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.ticket.Ticket;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.SeatEvent;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketCancelRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketCancelResponse;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import io.chan.queuingsystemforjava.domain.ticket.strategy.LockSeatStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketCancellationService {
    private final TicketRepository ticketRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    private final EventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final LockSeatStrategy lockSeatStrategy;
    private final PaymentProcessor paymentProcessor;
    @Transactional
    public TicketCancelResponse cancelTicket(Member member, TicketCancelRequest cancelRequest) {
        Ticket ticket = ticketRepository.findByIdWithPessimistic(cancelRequest.ticketId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_TICKET));
        if (!ticket.isOwnedBy(member)) {
            throw new TicketingException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Order order = orderRepository.findByIdWithPessimistic(ticket.getOrderId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));
        Seat seat = lockSeatStrategy.getSeatWithLock(order.getSeatId())
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_SEAT));

        Payment payment = paymentJpaRepository.findByOrder(order)
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PAYMENT));

        paymentProcessor.cancelPayment(cancelRequest, payment);

        ticket.markAsCancelled();
        seat.releaseSeat(member);
        order.markAsCancelled();

        eventPublisher.publish(new SeatEvent(member.getEmail(), seat.getSeatId(), SeatEvent.EventType.RELEASE));

        return new TicketCancelResponse(ticket.getTicketId());
    }

}
