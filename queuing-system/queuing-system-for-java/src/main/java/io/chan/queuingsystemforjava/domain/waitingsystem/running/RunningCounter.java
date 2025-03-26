package io.chan.queuingsystemforjava.domain.waitingsystem.running;

public interface RunningCounter {
    void increment(long performanceId, int number);

    long getRunningCount(long performanceId);
}