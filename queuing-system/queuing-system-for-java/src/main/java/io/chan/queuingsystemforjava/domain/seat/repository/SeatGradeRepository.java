package io.chan.queuingsystemforjava.domain.seat.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatGradeRepository {
    private final SeatGradeJpaRepository seatGradeJpaRepository;
    public List<SeatGrade> findAllByPerformance(Performance performance) {
        return seatGradeJpaRepository.findAllByPerformance(performance);
    }

    public void saveAll(List<SeatGrade> seatGrades) {
        seatGradeJpaRepository.saveAll(seatGrades);
    }

    public List<SeatGrade> findByPerformanceIdAndGradeNames(long performanceId, List<Long> gradeIds) {
        return seatGradeJpaRepository.findByPerformanceIdAndGradeNames(performanceId, gradeIds);
    }

    public List<SeatGrade> findAll() {
        return seatGradeJpaRepository.findAll();
    }
}
