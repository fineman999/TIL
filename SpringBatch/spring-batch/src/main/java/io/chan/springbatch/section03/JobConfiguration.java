package io.chan.springbatch.section03;

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

@Configuration
public class JobConfiguration {

    @Bean
    public Job testJob(JobRepository jobRepository, Step testStep1, Step testStep2) {
        return new JobBuilder("testJob", jobRepository)
                .start(testStep1)
                .next(testStep2)
                .build();
    }

    @Bean
    public Step testStep1(JobRepository jobRepository, Tasklet testTask1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep1", jobRepository)
                .tasklet(testTask1, transactionManager)
                .build();
    }

    @Bean
    public Step testStep2(JobRepository jobRepository, Tasklet testTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep2", jobRepository)
                .tasklet(testTask2, transactionManager)
                .build();
    }

    @Bean
    public Tasklet testTask1() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Hello, World!");
            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet testTask2() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Step 2 was executed!");
            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
