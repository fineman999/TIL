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
public class JobExecutionConfiguration {

    @Bean
    public Job executionJob(JobRepository jobRepository, Step executionStep1, Step executionStep2) {
        return new JobBuilder("executionJob", jobRepository)
                .start(executionStep1)
                .next(executionStep2)
                .build();
    }

    @Bean
    public Step executionStep1(JobRepository jobRepository, Tasklet executionTask1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionStep1", jobRepository)
                .tasklet(executionTask1, transactionManager)
                .build();
    }

    @Bean
    public Step executionStep2(JobRepository jobRepository, Tasklet executionTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionStep2", jobRepository)
                .tasklet(executionTask2, transactionManager)
                .build();
    }

    @Bean
    public Tasklet executionTask1() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Hello, World! Using Execution Test!");
            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet executionTask2() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Step 2 was executed! Using Execution Test!");
//            throw new RuntimeException("Step 2 was failed!");
//            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
