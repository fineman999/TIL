package io.chan.springbatchtestservice.controller;

import io.chan.springbatchtestservice.request.FileJobInfo;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {
  private final JobRegistry jobRegistry;
  private final JobExplorer jobExplorer;
  private final JobOperator jobOperator;

  @PostMapping("/api/jobs/fileJob/start")
  public ResponseEntity<String> startFileJob(@RequestBody FileJobInfo fileJobInfo)
      throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
    final String fileJob =
        jobRegistry.getJobNames().stream()
            .filter(jobName -> jobName.equals("fileJob"))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Not found job"));
    Properties properties = new Properties();
    properties.setProperty("requestDate", fileJobInfo.requestDate());
    properties.setProperty("date", new Date().getTime() + "");
    jobOperator.start(fileJob, properties);
    return ResponseEntity.ok("startFileJob");
  }
    @PostMapping("/api/jobs/fileJob/stop")
    public ResponseEntity<String> stopFileJob()
            throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        final String fileJob =
                jobRegistry.getJobNames().stream()
                        .filter(jobName -> jobName.equals("fileJob"))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Not found job"));
        final Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(fileJob);
        for (JobExecution runningJobExecution : runningJobExecutions) {
            jobOperator.stop(runningJobExecution.getId());
        }
        return ResponseEntity.ok("stopFileJob");
    }

    // 재시작이 되는 조건은 해당 JobInstance가 실패 혹은 중지된 상태여야 한다.
    @PostMapping("/api/jobs/fileJob/restart")
    public ResponseEntity<String> restartFileJob()
            throws NoSuchJobException, JobParametersInvalidException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobRestartException {
        final String fileJob =
                jobRegistry.getJobNames().stream()
                        .filter(jobName -> jobName.equals("fileJob"))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Not found job"));
        final JobInstance lastJobInstance = jobExplorer.getLastJobInstance(fileJob);
        assert lastJobInstance != null;
        final JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
        assert lastJobExecution != null;
        jobOperator.restart(lastJobExecution.getId());

        return ResponseEntity.ok("restartFileJob");
    }


}
