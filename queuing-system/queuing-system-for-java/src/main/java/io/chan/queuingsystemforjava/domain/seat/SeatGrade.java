package io.chan.queuingsystemforjava.domain.seat;

import io.chan.queuingsystemforjava.common.BaseEntity;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "seat_grades")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE seat_grades SET deleted_at = NOW() WHERE seat_grade_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class SeatGrade extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatGradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, length = 32)
    private String gradeName;
}
