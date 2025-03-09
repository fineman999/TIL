package io.chan.queuingsystemforjava.domain.zone;

import io.chan.queuingsystemforjava.common.BaseEntity;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "zones")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE zones SET deleted_at = NOW() WHERE zone_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Zone extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    private String zoneName;
}
