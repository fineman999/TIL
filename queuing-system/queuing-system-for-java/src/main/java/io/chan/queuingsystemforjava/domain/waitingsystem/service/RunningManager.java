package io.chan.queuingsystemforjava.domain.waitingsystem.service;

import java.util.Set;

public interface RunningManager {
    boolean isInRunningRoom(String email, long performanceId);

    long getRunningCount(long performanceId);

    long getAvailableToRunning(long performanceId);

    void enterRunningRoom(long performanceId, Set<String> emails);

    void pullOutRunningMember(String email, long performanceId);

    Set<String> removeExpiredMemberInfo(long performanceId);

    void updateRunningMemberExpiredTime(String email, long performanceId);
}