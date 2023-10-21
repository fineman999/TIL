package io.chan.springbatch.section03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class StepExecutionConfiguration {

    @Bean
    public Job stepExecutionJob(JobRepository jobRepository, Step stepExecutionStep1, Step stepExecutionStep2) {
        return new JobBuilder("stepExecutionJob", jobRepository)
                .start(stepExecutionStep1)
                .next(stepExecutionStep2)
                .build();
    }

    @Bean
    public Step stepExecutionStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepExecutionStep1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step stepExecutionStep2(JobRepository jobRepository, Tasklet stepExecutionTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepExecutionStep2", jobRepository)
                .tasklet(stepExecutionTask2, transactionManager)
                .build();
    }


    @Bean
    public Tasklet stepExecutionTask2() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 2 was executed! Using Step Test!");
            log.info("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
