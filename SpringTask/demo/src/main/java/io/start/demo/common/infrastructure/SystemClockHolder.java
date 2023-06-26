package io.start.demo.common.infrastructure;

import io.start.demo.common.service.port.ClockHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

@Component
public class SystemClockHolder implements ClockHolder {

    @Value("${security.jwt.expiration-time-access}")
    private long expirationAccess;

    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }

    @Override
    public Date now() {
        return new Date(millis());
    }

    @Override
    public Date expirationAccess() {
        return new Date(millis() + expirationAccess);
    }
}
