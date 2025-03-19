package io.chan.queuingsystemforjava.domain.zone.service;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.zone.dto.ZoneElement;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserZoneService {

    private final ZoneRepository zoneRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public ItemResult<ZoneElement> getZones(Long performanceId) {
        Performance performance =
                performanceRepository
                        .findById(performanceId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));
        return ItemResult.of(
                zoneRepository.findByPerformance(performance).stream()
                        .map(ZoneElement::of)
                        .toList());
    }
}