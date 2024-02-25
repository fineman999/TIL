package io.chan.springbatch.section06.exitstatus;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExitStatusConfiguration {

    @Bean
    public Job flowTransition(JobRepository jobRepository, Step step1, Step step2, Step step3, Step step4) {
        return new JobBuilder("flowTransitionJob", jobRepository)
                .start(step1)
                    .on("FAILED")
                    .to(step2)
                    .on("PASS")
                    .stop()
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    System.out.println("Step 1 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 2 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new PassCheckingListener())
                .build();
    }
}
