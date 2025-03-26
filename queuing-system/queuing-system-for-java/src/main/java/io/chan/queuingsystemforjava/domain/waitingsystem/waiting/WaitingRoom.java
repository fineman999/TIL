package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import java.util.Set;

public interface WaitingRoom {
    boolean enter(String email, long performanceId);

    void updateMemberInfo(String email, long performanceId, long waitingCount);

    void removeMemberInfo(String email, long performanceId);

    void removeMemberInfo(Set<String> emails, long performanceId);

    long getMemberWaitingCount(String email, long performanceId);
}