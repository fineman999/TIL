package io.chan.queuingsystemforjava.domain.waitingsystem.listener;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.ticket.dto.event.PaymentEvent;
import io.chan.queuingsystemforjava.domain.waitingsystem.dto.LastPollingEvent;
import io.chan.queuingsystemforjava.domain.waitingsystem.dto.PollingEvent;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@RequiredArgsConstructor
public class WaitingEventListener {

    private final WaitingSystem waitingSystem;

    @EventListener(PollingEvent.class)
    public void moveUserToRunningRoom(PollingEvent event) {
        waitingSystem.moveUserToRunning(event.performanceId());
    }

    @EventListener(LastPollingEvent.class)
    public void updateRunningMemberExpiredTime(LastPollingEvent event) {
        waitingSystem.updateRunningMemberExpiredTime(event.email(), event.performanceId());
    }

    @TransactionalEventListener(PaymentEvent.class)
    public void pullOutRunningMember(PaymentEvent event) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        long performanceId =
                Optional.ofNullable(request.getHeader("performanceId"))
                        .map(Long::parseLong)
                        .orElseThrow(
                                () ->
                                        new TicketingException(
                                                ErrorCode.NOT_CONTAINS_PERFORMANCE_INFO));
        waitingSystem.pullOutRunningMember(event.email(), performanceId);
    }
}