package io.chan.springbatch.section06.jobexecutiondecider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobExecutionDeciderConfiguration {

    @Bean
    public Job flowTransition(JobRepository jobRepository, Step stepStart, Step evenStep, Step oddStep, JobExecutionDecider decider) {
        return new JobBuilder("flowTransitionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepStart)
                .next(decider)
                .from(decider).on("EVEN").to(evenStep)
                .from(decider).on("ODD").to(oddStep)
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new CustomDecider();
    }

    @Bean
    public Step stepStart(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepStart", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is a step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step evenStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("evenStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is an even step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step oddStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("oddStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is an odd step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
