package io.chan.queuingsystemforjava.domain.waitingsystem.running;

import java.util.Set;

public interface RunningRoom {
    boolean contains(String email, long performanceId);

    long getAvailableToRunning(long performanceId);

    void enter(long performanceId, Set<String> emails);

    void pullOutRunningMember(String email, long performanceId);

    Set<String> removeExpiredMemberInfo(long performanceId);

    void updateRunningMemberExpiredTime(String email, long performanceId);
}