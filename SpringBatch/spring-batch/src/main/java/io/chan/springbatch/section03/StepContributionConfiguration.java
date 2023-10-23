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
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Component
public class StepContributionConfiguration {

    @Bean
    public Job stepContributionJob(JobRepository jobRepository, Step stepContributionStep1, Step stepContributionStep2) {
        return new JobBuilder("stepContributionJob", jobRepository)
                .start(stepContributionStep1)
                .next(stepContributionStep2)
                .build();
    }

    @Bean
    public Step stepContributionStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepContributionStep1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step stepContributionStep2(JobRepository jobRepository, Tasklet stepContributionTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepContributionStep2", jobRepository)
                .tasklet(stepContributionTask2, transactionManager)
                .build();
    }


    @Bean
    public Tasklet stepContributionTask2() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 2 was executed! Using Step Test!");
            log.info("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

}
