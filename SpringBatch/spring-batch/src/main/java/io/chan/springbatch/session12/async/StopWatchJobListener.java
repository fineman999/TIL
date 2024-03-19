package io.chan.springbatch.session12.async;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.util.Objects;

public class StopWatchJobListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {
        Duration duration = Duration.between(Objects.requireNonNull(jobExecution.getStartTime()), jobExecution.getEndTime());
        System.out.println("-----------------");
        System.out.println("Job duration: " + duration.toMillis() + "ms");
        System.out.println("-----------------");
    }
}
