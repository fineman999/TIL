package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import io.chan.queuingsystemforjava.domain.payment.repository.PaymentJpaRepository;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCancellationService;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import io.chan.queuingsystemforjava.domain.ticket.service.TicketCancellationService;
import io.chan.queuingsystemforjava.domain.ticket.strategy.LockSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.PessimisticLockSeatStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketCancellationServiceConfig {


}
