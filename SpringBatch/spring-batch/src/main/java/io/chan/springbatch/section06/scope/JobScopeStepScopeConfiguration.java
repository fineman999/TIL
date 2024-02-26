package io.chan.springbatch.section06.scope;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobScopeStepScopeConfiguration {

    @Bean
    public Job jobScopeStepScope(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("jobScopeStepScope", jobRepository)
                .start(step1)
                .next(step2)
                .listener(new CustomJobListener())
                .build();
    }

    @JobScope
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      @Value("#{jobParameters['message']}") String message) {
        System.out.println("message = " + message);
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("This is step 1");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet customTasklet) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(customTasklet, transactionManager)
                .listener(new CustomStepListener())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet customTasklet(@Value("#{stepExecutionContext['name']}") String name) {
        System.out.println("name = " + name);
        return (contribution, chunkContext) -> {
            System.out.println("tasklet bean was executed!");
            return RepeatStatus.FINISHED;
        };
    }


}
