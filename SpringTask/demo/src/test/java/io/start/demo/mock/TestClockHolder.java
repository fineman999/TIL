package io.start.demo.mock;

import io.start.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long millis;
    @Override
    public long millis() {
        return millis;
    }
}
