package io.chan.queuingsystemforjava.domain.performance;

import io.chan.queuingsystemforjava.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Entity
@Table(name = "performances")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE performances SET deleted_at = NOW() WHERE performance_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Performance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    private String performanceName;

    private String performancePlace;

    private ZonedDateTime performanceShowtime;
}
