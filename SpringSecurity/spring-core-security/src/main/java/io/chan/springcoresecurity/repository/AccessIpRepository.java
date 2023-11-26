package io.chan.springcoresecurity.repository;

import io.chan.springcoresecurity.domain.entity.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {
    AccessIp findByIpAddress(String ipAddress);
}
