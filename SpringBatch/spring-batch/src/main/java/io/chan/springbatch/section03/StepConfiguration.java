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
public class StepConfiguration {

    @Bean
    public Job stepJob(JobRepository jobRepository, Step stepStep1, Step stepStep2) {
        return new JobBuilder("stepJob", jobRepository)
                .start(stepStep1)
                .next(stepStep2)
                .build();
    }

    @Bean
    public Step stepStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepStep1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step stepStep2(JobRepository jobRepository, Tasklet stepTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepStep2", jobRepository)
                .tasklet(stepTask2, transactionManager)
                .build();
    }


    @Bean
    public Tasklet stepTask2() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 2 was executed! Using Step Test!");
            log.info("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
