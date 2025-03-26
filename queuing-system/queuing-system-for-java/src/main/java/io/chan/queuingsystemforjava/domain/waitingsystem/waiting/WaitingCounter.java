package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

public interface WaitingCounter {
    long getNextCount(long performanceId);
}
