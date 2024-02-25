package io.chan.springbatch.section06.flowjob;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowJobConfiguration {
    @Bean
    public Job flowJobBuilder(JobRepository jobRepository, Step flowJobStep1, Step flowJobStep2, Step flowJobStep3) {
        return new JobBuilder("stepBuilderJob", jobRepository)
                .start(flowJobStep1)
                .on("COMPLETED").to(flowJobStep2)
                .from(flowJobStep1).on("FAILED").to(flowJobStep3)
                .end()
                .build();
    }

    @Bean
    public Step flowJobStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep1", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step 1 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }

    @Bean
    public Step flowJobStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep2", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step 2 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }

    @Bean
    public Step flowJobStep3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep3", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step 3 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }
}
