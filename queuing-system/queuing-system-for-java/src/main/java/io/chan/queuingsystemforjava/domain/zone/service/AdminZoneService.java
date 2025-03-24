package io.chan.queuingsystemforjava.domain.zone.service;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.dto.CreateZoneRequest;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminZoneService {
    private final ZoneRepository zoneRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public void createZones(Long performanceId, CreateZoneRequest createZoneRequest) {
        Performance performance = performanceRepository.findById(performanceId).orElseThrow();
        List<Zone> zones = convertDtoToEntity(performance, createZoneRequest);
        zoneRepository.saveAll(zones);
    }

    private List<Zone> convertDtoToEntity(
            Performance performance, CreateZoneRequest createZoneRequest) {
        return createZoneRequest.zones().stream()
                .map(
                        zoneElement ->
                                Zone.builder()
                                        .performance(performance)
                                        .zoneName(zoneElement.zoneName())
                                        .build())
                .toList();
    }
}