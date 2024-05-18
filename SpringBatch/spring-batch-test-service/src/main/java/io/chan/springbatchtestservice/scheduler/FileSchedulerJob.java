package io.chan.springbatchtestservice.scheduler;

import java.util.Date;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class FileSchedulerJob extends QuartzJobBean {
  private final Job fileJob;
  private final JobLauncher jobLauncher;

  public FileSchedulerJob(@Qualifier("fileJob") final Job fileJob, final JobLauncher jobLauncher) {
    this.fileJob = fileJob;
    this.jobLauncher = jobLauncher;
  }

  @SneakyThrows
  @Override
  protected void executeInternal(final JobExecutionContext context) {

    final String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");

    final JobParameters jobParameters =
        new JobParametersBuilder()
            .addLong("id", new Date().getTime())
            .addString("requestDate", requestDate)
            .toJobParameters();

    jobLauncher.run(fileJob, jobParameters);
  }
}
