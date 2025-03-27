package io.chan.queuingsystemforjava.domain.waitingsystem.service;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.waitingsystem.dto.LastPollingEvent;
import io.chan.queuingsystemforjava.domain.waitingsystem.dto.PollingEvent;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WaitingSystem {

    private final Map<Long, PollingEvent> pollingEventCache = new ConcurrentHashMap<>();
    private final WaitingManager waitingManager;
    private final RunningManager runningManager;
    private final EventPublisher eventPublisher;

    public boolean isInRunningRoom(String email, long performanceId) {
        return runningManager.isInRunningRoom(email, performanceId);
    }

    public void enterWaitingRoom(String email, long performanceId) {
        waitingManager.enterWaitingRoom(email, performanceId);
    }

    public long pollRemainingCountAndTriggerEvents(String email, long performanceId) {
        long memberWaitingCount = waitingManager.getMemberWaitingCount(email, performanceId);
        long runningCount = runningManager.getRunningCount(performanceId);
        long remainingCount = memberWaitingCount - runningCount;

        // PollingEvent 객체를 생성하고 이벤트를 발행한다.
        PollingEvent pollingEvent =
                pollingEventCache.computeIfAbsent(performanceId, PollingEvent::new);
        // processExpiredAndMoveUsersToRunning를 비동기로 호출한다.
        eventPublisher.publish(pollingEvent);
        if (remainingCount <= 0) {
            // updateRunningMemberExpiredTime 메서드를 비동기로 호출
            eventPublisher.publish(new LastPollingEvent(email, performanceId));
        }
        return remainingCount;
    }

    public void processExpiredAndMoveUsersToRunning(long performanceId) {
        Set<String> removeMemberEmails = runningManager.removeExpiredMemberInfo(performanceId);
        if (!removeMemberEmails.isEmpty()) {
            waitingManager.removeMemberInfo(removeMemberEmails, performanceId);
        }

        long availableToRunning = runningManager.getAvailableToRunning(performanceId);
        Set<String> emails = waitingManager.pullOutMemberEmails(performanceId, availableToRunning);
        if (!emails.isEmpty()) {
            runningManager.enterRunningRoom(performanceId, emails);
        }
    }

    public void pullOutRunningMember(String email, long performanceId) {
        runningManager.pullOutRunningMember(email, performanceId);
        waitingManager.removeMemberInfo(email, performanceId);
    }

    /**
     * 공연에 해당하는 사용자의 작업 공간 만료 시간을 5분 뒤로 업데이트한다.
     *
     * @param email         사용자의 이메일
     * @param performanceId 공연 ID
     */
    public void updateRunningMemberExpiredTime(String email, long performanceId) {
        runningManager.updateRunningMemberExpiredTime(email, performanceId);
    }
}