package io.chan.springngrindertest.repository;

import io.chan.springngrindertest.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByCreateDateBetween(ZonedDateTime startDate, ZonedDateTime endDate);
}
