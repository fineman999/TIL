package io.chan.springbatch;

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
public class HelloJobConfiguration {

    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep1, Step helloStep2) {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1)
                .next(helloStep2)
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, Tasklet myTasklet1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep1", jobRepository)
                .tasklet(myTasklet1, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

    @Bean
    public Step helloStep2(JobRepository jobRepository, Tasklet myTasklet2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet(myTasklet2, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }


    @Bean
    public Tasklet myTasklet1() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Hello, World!");
            log.info("--------------------");
            return RepeatStatus.FINISHED; // RepeatStatus.CONTINUABLE
        };
    }


    @Bean
    public Tasklet myTasklet2() {
        return (contribution, chunkContext) -> {
             log.info("--------------------");
             log.info("Step 2 was executed!");
             log.info("--------------------");
            return RepeatStatus.FINISHED; // RepeatStatus.CONTINUABLE
        };
    }

}
