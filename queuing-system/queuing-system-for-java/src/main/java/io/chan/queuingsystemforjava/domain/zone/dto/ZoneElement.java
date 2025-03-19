package io.chan.queuingsystemforjava.domain.zone.dto;

import io.chan.queuingsystemforjava.domain.zone.Zone;

public record ZoneElement(
        Long id,
        String zoneName
) {
    public static ZoneElement of(Zone zone) {
        return new ZoneElement(zone.getZoneId(), zone.getZoneName());
    }
}
