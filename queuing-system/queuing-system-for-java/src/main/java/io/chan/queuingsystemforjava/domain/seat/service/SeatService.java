package io.chan.queuingsystemforjava.domain.seat.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatElement;
import io.chan.queuingsystemforjava.domain.seat.dto.response.SeatGradeElement;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final PerformanceRepository performanceRepository;
    private final ZoneRepository zoneRepository;
    private final SeatGradeRepository seatGradeRepository;
    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public ItemResult<SeatElement> getSeats(Long zoneId) {
        Zone zone =
                zoneRepository
                        .findById(zoneId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_ZONE));
        List<SeatElement> seats =
                seatRepository.findByZone(zone).stream().map(SeatElement::fromSeat).toList();

        return ItemResult.of(seats);
    }

    @Transactional(readOnly = true)
    public ItemResult<SeatGradeElement> getSeatGrades(Long performanceId) {
        Performance performance =
                performanceRepository
                        .findById(performanceId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_ZONE));
        List<SeatGradeElement> seatGrades =
                seatGradeRepository.findAllByPerformance(performance).stream()
                        .map(SeatGradeElement::of)
                        .toList();

        return ItemResult.of(seatGrades);
    }

    public ItemResult<SeatElement> getAllSeats(long performanceId) {
        List<SeatElement> seats =
                seatRepository.findByPerformanceId(performanceId).stream()
                        .map(SeatElement::fromSeat)
                        .toList();
        return ItemResult.of(seats);
    }
}