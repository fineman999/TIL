package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import java.util.Set;

public interface WaitingLine {
    void enter(String email, long performanceId, long waitingCount);

    Set<String> pullOutMembers(long performanceId, long availableToRunning);
}
