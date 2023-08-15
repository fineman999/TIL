package org.hello.chapter04.item20.defaultmethod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface TimeClient {
    void setTime(int hour, int minute, int second);

    void setDate(int day, int month, int year);

    void setDateAndTime(int day, int month, int year, int hour, int minute, int second);

    LocalDateTime getLocalDateTime();
    default void setDefaultTime() {
        setTime(0, 0, 0);
    }

    default void setDefaultDate() {
        setDate(1, 1, 1970);
    }

    default void setDefaultDateAndTime() {
        setDateAndTime(1, 1, 1970, 0, 0, 0);
    }

    static ZoneId getZoneId(String zoneString) {
        try {
            return ZoneId.of(zoneString);
        } catch (Exception e) {
            System.err.println("Invalid time zone: " + zoneString + "; using default time zone instead.");
            return ZoneId.systemDefault();
        }
    }
    default ZonedDateTime getZonedDateTime(String zoneString) {
        return ZonedDateTime.of(getLocalDateTime(), getZoneId(zoneString));
    }
}
