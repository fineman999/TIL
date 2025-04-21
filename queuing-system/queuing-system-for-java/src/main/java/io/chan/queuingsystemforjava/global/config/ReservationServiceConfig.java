package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.domain.order.repository.OrderRepository;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.ticket.proxy.DistributeLockReservationServiceProxy;
import io.chan.queuingsystemforjava.domain.ticket.proxy.OptimisticReservationServiceProxy;
import io.chan.queuingsystemforjava.domain.ticket.proxy.PessimisticReservationServiceProxy;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationManager;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationService;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationTransactionService;
import io.chan.queuingsystemforjava.domain.ticket.service.TicketCancellationService;
import io.chan.queuingsystemforjava.domain.ticket.strategy.LockSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.NaiveSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.OptimisticLockSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.PessimisticLockSeatStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class ReservationServiceConfig {

  @Value("${ticketing.reservation.release-delay-seconds}")
  private int reservationReleaseDelay;

  @Bean
  ReservationService optimisticReservationServiceProxy(
      @Qualifier("persistenceOptimisticReservationService")
          ReservationTransactionService persistenceOptimisticReservationService) {
    return new OptimisticReservationServiceProxy(persistenceOptimisticReservationService);
  }

  @Bean
  ReservationService pessimisticReservationServiceProxy(
      @Qualifier("persistencePessimisticReservationService")
          ReservationTransactionService persistencePessimisticReservationService) {
    return new PessimisticReservationServiceProxy(persistencePessimisticReservationService);
  }

  @Bean
  ReservationService distributedLockReservationServiceProxy(
      @Qualifier("distributedLockReservationService")
          ReservationTransactionService distributedLockReservationService) {
    return new DistributeLockReservationServiceProxy(distributedLockReservationService);
  }

  @Bean
  public ReservationTransactionService persistenceOptimisticReservationService(
      TicketRepository ticketRepository,
      PaymentProcessor paymentProcessor,
      MemberRepository memberRepository,
      SeatGradeRepository seatGradeRepository,
      SeatRepository seatRepository,
      OrderRepository orderRepository,
      EventPublisher eventPublisher,
      ReservationManager reservationManager,
      TaskScheduler taskScheduler) {
    LockSeatStrategy lockSeatStrategy = new OptimisticLockSeatStrategy(seatRepository);
    return new ReservationTransactionService(
        ticketRepository,
        memberRepository,
        seatGradeRepository,
        paymentProcessor,
        orderRepository,
        lockSeatStrategy,
        eventPublisher,
        reservationManager,
        reservationReleaseDelay,
        taskScheduler);
  }

  @Primary
  @Bean
  public ReservationTransactionService persistencePessimisticReservationService(
      TicketRepository ticketRepository,
      PaymentProcessor paymentProcessor,
      MemberRepository memberRepository,
      SeatGradeRepository seatGradeRepository,
      OrderRepository orderRepository,
      SeatRepository seatRepository,
      EventPublisher eventPublisher,
      ReservationManager reservationManager,
      TaskScheduler scheduler) {
    LockSeatStrategy lockSeatStrategy = new PessimisticLockSeatStrategy(seatRepository);
    return new ReservationTransactionService(
        ticketRepository,
        memberRepository,
        seatGradeRepository,
        paymentProcessor,
        orderRepository,
        lockSeatStrategy,
        eventPublisher,
        reservationManager,
        reservationReleaseDelay,
        scheduler);
  }

  @Bean
  public ReservationTransactionService distributedLockReservationService(
      TicketRepository ticketRepository,
      PaymentProcessor paymentProcessor,
      MemberRepository memberRepository,
      SeatGradeRepository seatGradeRepository,
      OrderRepository orderRepository,
      SeatRepository seatRepository,
      EventPublisher eventPublisher,
      ReservationManager reservationManager,
      TaskScheduler scheduler) {
    LockSeatStrategy lockSeatStrategy = new NaiveSeatStrategy(seatRepository);
    return new ReservationTransactionService(
        ticketRepository,
        memberRepository,
        seatGradeRepository,
        paymentProcessor,
        orderRepository,
        lockSeatStrategy,
        eventPublisher,
        reservationManager,
        reservationReleaseDelay,
        scheduler);
  }
}
