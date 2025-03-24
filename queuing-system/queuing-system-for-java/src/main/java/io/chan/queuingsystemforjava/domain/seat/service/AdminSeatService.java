package io.chan.queuingsystemforjava.domain.seat.service;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatCreationElement;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.dto.request.SeatGradeCreationRequest;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatGradeRepository;
import io.chan.queuingsystemforjava.domain.seat.repository.SeatRepository;
import io.chan.queuingsystemforjava.domain.zone.Zone;
import io.chan.queuingsystemforjava.domain.zone.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSeatService {
    private final SeatRepository seatRepository;
    private final SeatGradeRepository seatGradeRepository;
    private final PerformanceRepository performanceRepository;
    private final ZoneRepository zoneRepository;

    @Transactional
    public void createSeatGrades(
            long performanceId, SeatGradeCreationRequest seatGradeCreationRequest) {
        List<SeatGrade> seatGrades = convertDtoToEntity(performanceId, seatGradeCreationRequest);

        seatGradeRepository.saveAll(seatGrades);
    }

    private List<SeatGrade> convertDtoToEntity(
            long performanceId, SeatGradeCreationRequest seatGradeCreationRequest) {
        Performance performance =
                performanceRepository
                        .findById(performanceId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));

        return seatGradeCreationRequest.seatGradeCreationElements().stream()
                .map(
                        seatGrade ->
                                SeatGrade.builder()
                                        .performance(performance)
                                        .price(seatGrade.price())
                                        .gradeName(seatGrade.seatGradeName())
                                        .build())
                .toList();
    }

    @Transactional
    public void createSeats(
            long performanceId, long zoneId, SeatCreationRequest seatCreationRequest) {
        List<Seat> seats = convertDtoToEntity(performanceId, zoneId, seatCreationRequest);
        seatRepository.saveAll(seats);
    }

    private List<Seat> convertDtoToEntity(
            long performanceId, long zoneId, SeatCreationRequest seatCreationRequest) {
        Map<Long, SeatGrade> seatGradeMap = findSeatGrades(performanceId, seatCreationRequest);

        Zone zone =
                zoneRepository
                        .findById(zoneId)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_ZONE));

        return seatCreationRequest.seatCreationElements().stream()
                .map(
                        seat ->
                                Seat.builder()
                                        .zone(zone)
                                        .seatGrade(seatGradeMap.get(seat.seatGradeId()))
                                        .seatCode(seat.seatCode())
                                        .build())
                .toList();
    }

    /** N+1 문제를 방지하기 위해 요청된 gradeName 목록을 미리 조회합니다. Map<gradeName, SeatGrade> 형태로 구조화해서 반환합니다. */
    private Map<Long, SeatGrade> findSeatGrades(
            long performanceId, SeatCreationRequest seatCreationRequest) {
        List<Long> gradeIds =
                seatCreationRequest.seatCreationElements().stream()
                        .map(SeatCreationElement::seatGradeId)
                        .distinct()
                        .toList();

        List<SeatGrade> seatGrades =
                seatGradeRepository.findByPerformanceIdAndGradeNames(performanceId, gradeIds);
        Map<Long, SeatGrade> seatGradeMap =
                seatGrades.stream()
                        .collect(
                                Collectors.toMap(
                                        SeatGrade::getSeatGradeId, seatGrade -> seatGrade));

        if (seatGradeMap.size() != gradeIds.size()) {
            throw new TicketingException(ErrorCode.NOT_FOUND_SEAT_GRADE);
        }

        return seatGradeMap;
    }
}