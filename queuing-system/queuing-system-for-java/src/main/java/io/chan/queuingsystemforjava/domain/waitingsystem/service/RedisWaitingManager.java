package io.chan.queuingsystemforjava.domain.waitingsystem.service;

import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingCounter;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingLine;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class RedisWaitingManager implements WaitingManager {

    private final RedisWaitingRoom waitingRoom;
    private final RedisWaitingCounter waitingCounter;
    private final RedisWaitingLine waitingLine;

    @Override
    public void enterWaitingRoom(String email, long performanceId) {
        if (waitingRoom.enter(email, performanceId)) {
            long waitingCount = waitingCounter.getNextCount(performanceId);
            waitingRoom.updateMemberInfo(email, performanceId, waitingCount);
            waitingLine.enter(email, performanceId, waitingCount);
        }
    }

    @Override
    public void removeMemberInfo(String email, long performanceId) {
        waitingRoom.removeMemberInfo(email, performanceId);
    }

    @Override
    public void removeMemberInfo(Set<String> emails, long performanceId) {
        waitingRoom.removeMemberInfo(emails, performanceId);
    }

    @Override
    public long getMemberWaitingCount(String email, long performanceId) {
        return waitingRoom.getMemberWaitingCount(email, performanceId);
    }

    @Override
    public Set<String> pullOutMemberEmails(long performanceId, long availableToRunning) {
        return waitingLine.pullOutMembers(performanceId, availableToRunning);
    }
}