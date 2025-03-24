package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.domain.payment.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.ticket.proxy.OptimisticReservationServiceProxy;
import io.chan.queuingsystemforjava.domain.ticket.proxy.PessimisticReservationServiceProxy;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationManager;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationService;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationTransactionService;
import io.chan.queuingsystemforjava.domain.ticket.strategy.LockSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.OptimisticLockSeatStrategy;
import io.chan.queuingsystemforjava.domain.ticket.strategy.PessimisticLockSeatStrategy;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ReservationServiceContainer {

    @Value("${ticketing.reservation.release-delay-seconds}")
    private int reservationReleaseDelay;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

//    @Bean
//    @Primary
//    public ReservationService newRedisReservationService(
//            MemberRepository memberRepository,
//            SeatRepository seatRepository,
//            StringRedisTemplate redisTemplate) {
//        return new NewRedisReservationService(memberRepository, seatRepository, redisTemplate, reservationReleaseDelay);
//    }


//    @Bean
//    public ReservationService redissonReservationServiceProxy(
//            RedissonClient redissonClient,
//            @Qualifier("cacheReservationTransactionService")
//                    ReservationTransactionService cacheReservationTransactionService) {
//        return new RedissonReservationServiceProxy(
//                redissonClient, cacheReservationTransactionService);
//    }

//    @Bean
//    public ReservationService lettuceReservationServiceProxy(
//            LettuceRepository lettuceRepository,
//            @Qualifier("cacheReservationTransactionService")
//                    ReservationTransactionService cacheReservationTransactionService) {
//        return new LettuceReservationServiceProxy(
//                lettuceRepository, cacheReservationTransactionService);
//    }

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

//    @Bean
//    public ReservationTransactionService cacheReservationTransactionService(
//            TicketRepository ticketRepository,
//            PaymentProcessor paymentProcessor,
//            MemberRepository memberRepository,
//            SeatRepository seatRepository,
//            EventPublisher eventPublisher,
//            ReservationManager reservationManager) {
//        LockSeatStrategy lockSeatStrategy = new NaiveSeatStrategy(seatRepository);
//        return new ReservationTransactionService(
//                ticketRepository,
//                memberRepository,
//                paymentProcessor,
//                lockSeatStrategy,
//                eventPublisher,
//                reservationManager,
//                reservationReleaseDelay,
//                scheduler
//        );
//    }

    @Bean
    public ReservationTransactionService persistenceOptimisticReservationService(
            TicketRepository ticketRepository,
            PaymentProcessor paymentProcessor,
            MemberRepository memberRepository,
            SeatRepository seatRepository,
            EventPublisher eventPublisher,
            ReservationManager reservationManager) {
        LockSeatStrategy lockSeatStrategy = new OptimisticLockSeatStrategy(seatRepository);
        return new ReservationTransactionService(
                ticketRepository,
                memberRepository,
                paymentProcessor,
                lockSeatStrategy,
                eventPublisher,
                reservationManager,
                reservationReleaseDelay,
                scheduler
        );
    }

    @Primary
    @Bean
    public ReservationTransactionService persistencePessimisticReservationService(
            TicketRepository ticketRepository,
            PaymentProcessor paymentProcessor,
            MemberRepository memberRepository,
            SeatRepository seatRepository,
            EventPublisher eventPublisher,
            ReservationManager reservationManager) {
        LockSeatStrategy lockSeatStrategy = new PessimisticLockSeatStrategy(seatRepository);
        return new ReservationTransactionService(
                ticketRepository,
                memberRepository,
                paymentProcessor,
                lockSeatStrategy,
                eventPublisher,
                reservationManager,
                reservationReleaseDelay,
                scheduler
        );
    }
}