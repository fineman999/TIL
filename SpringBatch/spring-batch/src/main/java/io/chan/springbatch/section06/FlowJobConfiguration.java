package io.chan.springbatch.section06;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowJobConfiguration {

    @Bean
    public Job flowTransition(JobRepository jobRepository, Flow flow, Step step2, Step step3, Step step4) {
        return new JobBuilder("flowTransitionJob", jobRepository)
                .start(flow)
                .next(step4)
                .end()
                .build();
    }

    @Bean
    public Flow flow(Step step2, Step step3) {
        return new FlowBuilder<Flow>("flow")
                .start(step2)
                .next(step3)
                .end();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 2 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 3 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Step 4 was executed!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
