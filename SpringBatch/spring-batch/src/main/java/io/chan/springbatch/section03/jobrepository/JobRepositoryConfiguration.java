package io.chan.springbatch.section03.jobrepository;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobRepositoryConfiguration {
    private final JobExecutionListener jobExecutionListener;

    @Bean
    public Job BatchJob(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("BatchJob", jobRepository)
                .start(step1)
                .next(step2)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> null, transactionManager)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
    @Bean
    public Flow flow1(Step step1, Step step2) {
        return new FlowBuilder<Flow>("flow1")
                .start(step1)
                .next(step2)
                .build();
    }
}
