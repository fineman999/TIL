package io.chan.springbatchtestservice.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class FileSchedulerJob extends QuartzJobBean {
  private final Job fileJob;
  private final JobLauncher jobLauncher;
  private final JobExplorer jobExplorer;

  public FileSchedulerJob(
      @Qualifier("fileJob") final Job fileJob,
      final JobLauncher jobLauncher,
      final JobExplorer jobExplorer) {
    this.fileJob = fileJob;
    this.jobLauncher = jobLauncher;
    this.jobExplorer = jobExplorer;
  }

  @SneakyThrows
  @Override
  protected void executeInternal(final JobExecutionContext context) {

    final String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

    final long jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
    final List<JobInstance> jobInstances =
        jobExplorer.getJobInstances(fileJob.getName(), 0, (int) jobInstanceCount);
    for (JobInstance jobInstance : jobInstances) {
      final List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
      final Optional<JobExecution> execution =
          jobExecutions.stream()
              .filter(
                  jobExecution ->
                      Objects.equals(
                          jobExecution.getJobParameters().getString("requestDate"), requestDate))
              .filter(jobExecution -> jobExecution.getStatus() == BatchStatus.COMPLETED)
              .findAny();
      if (execution.isPresent()) {
        throw new JobExecutionException(requestDate + " Job already exists");
      }
    }

    final JobParameters jobParameters =
        new JobParametersBuilder()
            .addLong("id", new Date().getTime())
            .addString("requestDate", requestDate)
            .toJobParameters();

    jobLauncher.run(fileJob, jobParameters);
  }
}
