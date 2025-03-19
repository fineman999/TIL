package io.chan.queuingsystemforjava.domain.zone.dto;

import io.chan.queuingsystemforjava.domain.zone.Zone;

import java.util.Collection;
import java.util.List;

public record CreateZoneRequest(
        List<Zone> zones
) {
}
