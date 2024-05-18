package io.chan.springbatchtestservice.scheduler;

import java.util.Date;
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
public class ApiSchedulerJob extends QuartzJobBean {
  private final Job apiJob;
  private final JobLauncher jobLauncher;

    public ApiSchedulerJob(
            @Qualifier("apiJob") final Job apiJob, final JobLauncher jobLauncher) {
        this.apiJob = apiJob;
        this.jobLauncher = jobLauncher;
    }

    @Override
  protected void executeInternal(final JobExecutionContext context) {
    final JobParameters jobParameters =
        new JobParametersBuilder().addLong("id", new Date().getTime()).toJobParameters();
    try {
      jobLauncher.run(apiJob, jobParameters);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
