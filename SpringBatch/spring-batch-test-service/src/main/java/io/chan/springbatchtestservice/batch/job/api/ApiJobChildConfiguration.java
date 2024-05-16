package io.chan.springbatchtestservice.batch.job.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiJobChildConfiguration {
  private final JobLauncher jobLauncher; // Job을 실행할 JobLauncher설정
  private final Step apiMasterStep; // JobStep 내에서 실행 될 Step 설정

  public ApiJobChildConfiguration(
      final JobLauncher jobLauncher, @Qualifier("apiMasterStep") final Step apiMasterStep) {
    this.jobLauncher = jobLauncher;
    this.apiMasterStep = apiMasterStep;
  }

  @Bean
  public Step jobStep(JobRepository jobRepository, @Qualifier("childJob") Job childJob) {
    return new StepBuilder("jobStep", jobRepository).job(childJob).launcher(jobLauncher).build();
  }

  // JobStep 내에서 실행 될 Job 설정
  @Bean
  public Job childJob(JobRepository jobRepository) {
    return new JobBuilder("childJob", jobRepository).start(apiMasterStep).build();
  }
}
