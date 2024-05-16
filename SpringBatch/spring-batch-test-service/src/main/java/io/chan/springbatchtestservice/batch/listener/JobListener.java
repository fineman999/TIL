package io.chan.springbatchtestservice.batch.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobListener implements JobExecutionListener {
    @Override
    public void afterJob(final JobExecution jobExecution) {
        final LocalDateTime startTime = jobExecution.getStartTime();
        final LocalDateTime endTime = jobExecution.getEndTime();
        Duration duration = Duration.between(Objects.requireNonNull(startTime), endTime);
        log.info("총 처리 시간: {}ms", duration.toMillis());
    }
}
