package io.chan.springbatchtestservice.batch.job.api;

import io.chan.springbatchtestservice.batch.listener.JobListener;
import io.chan.springbatchtestservice.batch.tasklet.ApiEndTasklet;
import io.chan.springbatchtestservice.batch.tasklet.ApiStartTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ApiJobConfiguration {
  private final ApiStartTasklet apiStartTasklet;
  private final ApiEndTasklet apiEndTasklet;
  private final Step jobStep;

  public ApiJobConfiguration(
      final ApiStartTasklet apiStartTasklet,
      final ApiEndTasklet apiEndTasklet,
      @Qualifier("jobStep") final Step jobStep) {
    this.apiStartTasklet = apiStartTasklet;
    this.apiEndTasklet = apiEndTasklet;
    this.jobStep = jobStep;
  }

  @Bean
  public Job apiJob(
      JobRepository jobRepository,
      @Qualifier("apiStep1") Step apiStep1,
      @Qualifier("apiStep2") Step apiStep2) {
    return new JobBuilder("apiJob", jobRepository)
        .listener(new JobListener()) // Job의 시작과 끝에 대한 이벤트를 처리
        .start(apiStep1)
        .next(jobStep)
        .next(apiStep2)
        .build();
  }

  @Bean
  public Step apiStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("apiStep1", jobRepository)
        .tasklet(apiStartTasklet, transactionManager)
        .build();
  }

  @Bean
  public Step apiStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("apiStep2", jobRepository)
        .tasklet(apiEndTasklet, transactionManager)
        .build();
  }
}
