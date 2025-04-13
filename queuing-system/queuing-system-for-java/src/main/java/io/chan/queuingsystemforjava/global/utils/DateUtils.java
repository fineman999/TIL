package io.chan.queuingsystemforjava.global.utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {
    public static ZoneId asiaSeoulZoneId = ZoneId.of("Asia/Seoul");
    private DateUtils() {
    }

    public static ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(asiaSeoulZoneId);
    }
}
