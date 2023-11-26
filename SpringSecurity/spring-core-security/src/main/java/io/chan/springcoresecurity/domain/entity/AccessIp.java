package io.chan.springcoresecurity.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "access_ip")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessIp {

    @Id
    @GeneratedValue
    @Column(name = "ip_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Builder
    public AccessIp(Long id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    public boolean isIpMatched(String remoteAddress) {
        return this.ipAddress.equals(remoteAddress);
    }
}
