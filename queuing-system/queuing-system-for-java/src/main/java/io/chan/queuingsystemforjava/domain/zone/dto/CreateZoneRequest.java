package io.chan.queuingsystemforjava.domain.zone.dto;

import java.util.List;

public record CreateZoneRequest(
        List<CreateZoneElement> zones
) {
}
