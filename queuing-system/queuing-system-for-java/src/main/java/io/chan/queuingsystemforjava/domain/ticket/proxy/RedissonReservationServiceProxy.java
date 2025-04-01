package io.chan.queuingsystemforjava.domain.ticket.proxy;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.SeatSelectionRequest;
import io.chan.queuingsystemforjava.domain.ticket.dto.request.TicketPaymentRequest;
import io.chan.queuingsystemforjava.domain.ticket.service.ReservationTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class RedissonReservationServiceProxy implements ReservationServiceProxy {

    private final RedissonClient redissonClient;
    private final ReservationTransactionService reservationTransactionService;

    private void performSeatAction(String seatId, Runnable action) {
        String lockPrefix = "seat:";
        RLock lock = redissonClient.getLock(lockPrefix + seatId);
        int tryTime = 1;
        int releaseTime = 60;
        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(tryTime, releaseTime, TimeUnit.SECONDS);
            if (!lockAcquired) {
                log.warn("Failed to acquire lock for seatId: {}", seatId);
                return;
            }
            action.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TicketingException(ErrorCode.NOT_SELECTABLE_SEAT, e);
        } finally {
            if (lockAcquired && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Lock released for seatId: {}", seatId);
            }
        }
    }

    @Override
    public void selectSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        performSeatAction(
                seatSelectionRequest.seatId().toString(),
                () -> reservationTransactionService.selectSeat(memberEmail, seatSelectionRequest));
    }

    @Override
    public void reservationTicket(String memberEmail, TicketPaymentRequest ticketPaymentRequest) {
        performSeatAction(
                ticketPaymentRequest.seatId().toString(),
                () ->
                        reservationTransactionService.reservationTicket(
                                memberEmail, ticketPaymentRequest));
    }

    @Override
    public void releaseSeat(String memberEmail, SeatSelectionRequest seatSelectionRequest) {
        reservationTransactionService.releaseSeat(memberEmail, seatSelectionRequest);
    }
}