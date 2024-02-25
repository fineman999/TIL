package io.chan.springbatch.section06.startnext;

import org.springframework.batch.core.Job;
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

@Configuration
public class FlowConfiguration2 {
    @Bean
    public Job flowJobBuilder(JobRepository jobRepository, Flow flowA, Step stepA, Flow flowB, Step stepB) {
        return new JobBuilder("stepBuilderJob", jobRepository)
                .start(flowA)
                .next(stepA)
                .next(flowB)
                .next(stepB)
                .end()
                .build();
    }

    @Bean
    public Flow flowA(Step step1, Step step2) {
        return new FlowBuilder<Flow>("flowA")
                .start(step1)
                .next(step2)
                .end();
    }

    @Bean
    public Flow flowB(Step step3, Step step4) {
        return new FlowBuilder<Flow>("flowB")
                .start(step3)
                .next(step4)
                .end();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
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
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
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
    public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
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

    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep4", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step 4 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }

    @Bean
    public Step stepA(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStepA", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step A was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }

    @Bean
    public Step stepB(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStepB", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step B was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .build(); // Step을 생성
    }
}
