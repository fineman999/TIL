package io.chan.springbatch.section02;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DBJobConfiguration {

    @Bean
    public Job dbJob(JobRepository jobRepository, Step dbStep1, Step dbStep2) {
        return new JobBuilder("dbJob", jobRepository)
                .start(dbStep1)
                .next(dbStep2)
                .build();
    }

    @Bean
    public Step dbStep1(JobRepository jobRepository, Tasklet dbTasklet1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dbStep1", jobRepository)
                .tasklet(dbTasklet1, transactionManager)
                .build();
    }

    @Bean
    public Step dbStep2(JobRepository jobRepository, Tasklet dbTasklet2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dbStep2", jobRepository)
                .tasklet(dbTasklet2, transactionManager)
                .build();
    }

    @Bean
    public Tasklet dbTasklet1() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("DB Step 1 was executed!");
            log.info("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet dbTasklet2() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("DB Step 2 was executed!");
            log.info("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

}
