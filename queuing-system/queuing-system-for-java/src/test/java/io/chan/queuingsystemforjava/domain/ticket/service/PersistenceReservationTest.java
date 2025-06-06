package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.global.config.ReservationServiceConfig;
import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ ReservationServiceConfig.class})
public class PersistenceReservationTest extends BaseIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(PersistenceReservationTest.class);

    @Autowired
    @Qualifier("optimisticReservationServiceProxy")
    private ReservationService optimisticReservationService;

    @Autowired
    @Qualifier("pessimisticReservationServiceProxy")
    private ReservationService pessimisticReservationService;

    @Autowired
    @Qualifier("distributedLockReservationServiceProxy")
    private ReservationService distributedLockReservationService;

    private final String memberEmail = "test@gmail.com";
    private final Long seatId = 1L;

    private final String paymentKey = UUID.randomUUID().toString();

    private final String orderId = "ORDER_1_1_20241212";
    private final BigDecimal price = BigDecimal.valueOf(10000);

    @Nested
    @DisplayName("티켓 예매를 위해 좌석을 선택할 때")
    @Sql(
            scripts = "/db/select-seat-test.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    class SelectSeatTest {
        @Nested
        @DisplayName("낙관 락을 사용하면")
        class OptimisticLockTest {
            @Test
            @DisplayName("여러개의 동시 요청 중 한 명만 좌석을 성공적으로 선택해야 한다.")
            void selectSeat_optimistic() throws InterruptedException {
                selectSeat_ConcurrencyTest(optimisticReservationService);
            }
        }

        @Nested
        @DisplayName("비관 락을 사용하면")
        class PessimisticLockTest {
            @Test
            @DisplayName("여러개의 동시 요청 중 한 명만 좌석을 성공적으로 선택해야 한다.")
            void selectSeat_optimistic() throws InterruptedException {
                selectSeat_ConcurrencyTest(pessimisticReservationService);
            }
        }

        @Nested
        @DisplayName("분산 락을 사용하면")
        class DistributedLockTest {
            @Test
            @DisplayName("여러개의 동시 요청 중 한 명만 좌석을 성공적으로 선택해야 한다.")
            void selectSeat_optimistic() throws InterruptedException {
                selectSeat_ConcurrencyTest(distributedLockReservationService);
            }
        }


        private void selectSeat_ConcurrencyTest(ReservationService reservationService)
                throws InterruptedException {
            // Given
            int numRequests = 1000;
            CountDownLatch latch = new CountDownLatch(1);
            ExecutorService executor = Executors.newFixedThreadPool(numRequests);

            AtomicInteger successfulSelections = new AtomicInteger(0);
            AtomicInteger failedSelections = new AtomicInteger(0);

            // when
            IntStream.range(0, numRequests)
                    .forEach(
                            i ->
                                    executor.submit(
                                            () ->
                                                    selectSeatTask(
                                                            reservationService,
                                                            latch,
                                                            seatId,
                                                            successfulSelections,
                                                            failedSelections)));

            latch.countDown(); // 모든 스레드가 동시에 실행되도록 설정

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // Then
            assertThat(successfulSelections.get()).isEqualTo(1);
//            assertThat(failedSelections.get()).isEqualTo(numRequests - 1);
        }

        private void selectSeatTask(
                ReservationService reservationService,
                CountDownLatch latch,
                Long seatId,
                AtomicInteger successfulSelections,
                AtomicInteger failedSelections) {

            setUpAuthentication();
            try {
                latch.await();
                try {
                    SeatSelectionRequest seatSelectionRequest = new SeatSelectionRequest(seatId);
                    reservationService.selectSeat(memberEmail, seatSelectionRequest);
                    successfulSelections.incrementAndGet();
                } catch (TicketingException e) {
                    log.error(e.getMessage(), e);
                    failedSelections.incrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void setUpAuthentication() {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            "test@gmail.com", "testpassword", List.of());
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
    }

    @Nested
    @DisplayName("티켓 예매 할 때 결제 시도 시")
    @Sql(
            scripts = "/db/reservation-test.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Transactional
    class reservationTicketTest {
        @Nested
        @DisplayName("낙관 락을 사용하면")
        class OptimisticLockTest {
            @Test
            @DisplayName("하나의 결제만 성공한다.")
            void reservationTicket_optimistic() throws InterruptedException {
                reservationTicket_ConcurrencyTest(optimisticReservationService);
            }
        }

        @Nested
        @DisplayName("비관 락을 사용하면")
        class PessimisticLockTest {
            @Test
            @DisplayName("하나의 결제만 성공한다.")
            void reservationTicket_pessimistic() throws InterruptedException {
                reservationTicket_ConcurrencyTest(pessimisticReservationService);
            }
        }

        @Nested
        @DisplayName("분산 락을 사용하면")
        class DistributedLockTest {
            @Test
            @DisplayName("하나의 결제만 성공한다.")
            void reservationTicket_distributed() throws InterruptedException {
                reservationTicket_ConcurrencyTest(distributedLockReservationService);
            }
        }

        @DisplayName("동시에 여러 요청이 오면 하나의 요청만 성공한다.")
        void reservationTicket_ConcurrencyTest(ReservationService reservationService)
                throws InterruptedException {
            // Given
            int numRequests = 1000;
            CountDownLatch latch = new CountDownLatch(1);
            ExecutorService executor = Executors.newFixedThreadPool(numRequests);

            AtomicInteger successfulReservations = new AtomicInteger(0);
            AtomicInteger failedReservations = new AtomicInteger(0);

            // When
            IntStream.range(0, numRequests)
                    .forEach(
                            i ->
                                    executor.submit(
                                            () -> {
                                                try {
                                                    latch.await(); // 동기화된 시작
                                                    reservationTicketTask(
                                                            reservationService,
                                                            memberEmail,
                                                            seatId,
                                                            paymentKey,
                                                            orderId,
                                                            price,
                                                            successfulReservations,
                                                            failedReservations);
                                                } catch (InterruptedException e) {
                                                    Thread.currentThread().interrupt();
                                                }
                                            }));

            latch.countDown(); // 모든 스레드가 동시에 실행되도록 설정

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            // Then
            assertThat(successfulReservations.get()).isEqualTo(1);
            assertThat(failedReservations.get()).isEqualTo(numRequests - 1);
        }

        private void reservationTicketTask(
                ReservationService reservationService,
                String memberEmail,
                Long seatId,
                String paymentKey,
                String orderId,
                BigDecimal price,
                AtomicInteger successfulReservations,
                AtomicInteger failedReservations) {
            try {
                TicketPaymentRequest ticketPaymentRequest = new TicketPaymentRequest(seatId, paymentKey, orderId, price);
                reservationService.reservationTicket(memberEmail, ticketPaymentRequest);
                successfulReservations.incrementAndGet();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                failedReservations.incrementAndGet();
            }
        }
    }
}