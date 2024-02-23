package io.chan.springbatch.section05.jobstep;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@EnableBatchProcessing(
        dataSourceRef = "batchDataSource",
        transactionManagerRef = "batchTransactionManager",
        tablePrefix = "BATCH_",
        maxVarCharLength = 1000,
        isolationLevelForCreate = "SERIALIZABLE")
@Configuration
public class JobStepConfiguration {

    private final JobRepository jobRepository;

    public JobStepConfiguration(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job parentJob(JobRepository jobRepository, Step jobStep1, Step jobStep2) {
        return new JobBuilder("jobStepJob", jobRepository)
                .start(jobStep1)
                .next(jobStep2)
                .build();
    }

    @Bean
    public Step jobStep1(JobRepository jobRepository, JobLauncher jobLauncher, Job chileJob) {
        return new StepBuilder("step1", jobRepository)
                .job(chileJob) //// JobStep 내 에서 실행 될 Job 설정, JobStepBuilder  반환
                .launcher(jobLauncher) //// Job 을 실행할 JobLauncher설정
                .parametersExtractor(jobParametersExtractor()) // Step의 ExecutionContext를 Job이 실행되는 데 필요한 JobParameters로 변환
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(final StepExecution stepExecution) {
                        // Step의 ExecutionContext에 name이라는 키로 chan이라는 값을 넣음
                        stepExecution.getJobExecution().getExecutionContext().put("name", "chan");
                    }

                    @Override
                    public ExitStatus afterStep(final StepExecution stepExecution) {
                        return StepExecutionListener.super.afterStep(stepExecution);
                    }
                })
                .build();
    }

    private JobParametersExtractor jobParametersExtractor() {
        final DefaultJobParametersExtractor defaultJobParametersExtractor = new DefaultJobParametersExtractor();
        // JobParametersExtractor가 사용할 키를 설정
        defaultJobParametersExtractor.setKeys(new String[]{"name"});
        return defaultJobParametersExtractor;
    }


    @Bean
    public Job childJob(JobRepository jobRepository, Step jobStep1, Step jobStep2) {
        return new JobBuilder("childJob", jobRepository)
                .start(jobStep1)
                .next(jobStep2)
                .build();
    }


    @Bean
    public Step jobStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 1 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
