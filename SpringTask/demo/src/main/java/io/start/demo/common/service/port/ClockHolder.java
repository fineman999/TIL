package io.start.demo.common.service.port;


import java.util.Date;

public interface ClockHolder {
    long millis();

    Date now();

    Date expirationAccess();
}
