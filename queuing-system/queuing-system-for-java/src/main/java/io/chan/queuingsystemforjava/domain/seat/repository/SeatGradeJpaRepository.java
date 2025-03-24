package io.chan.queuingsystemforjava.domain.seat.repository;

import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.seat.SeatGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatGradeJpaRepository extends JpaRepository<SeatGrade, Long> {

    @Query("SELECT sg FROM SeatGrade sg WHERE sg.performance = :performance")
    List<SeatGrade> findAllByPerformance(Performance performance);
    @Query(
            """
            SELECT sg
            FROM SeatGrade sg
            WHERE sg.performance.performanceId = :performanceId
            AND sg.seatGradeId IN :gradeIds
            """)
    List<SeatGrade> findByPerformanceIdAndGradeNames(long performanceId, List<Long> gradeIds);

}
