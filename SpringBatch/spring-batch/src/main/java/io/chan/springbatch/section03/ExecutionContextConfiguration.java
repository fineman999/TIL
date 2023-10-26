package io.chan.springbatch.section03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ExecutionContextConfiguration {

    @Bean
    public Job executionContextJob(JobRepository jobRepository, Step executionContextStep1, Step executionContextStep2, Step executionContextStep3, Step executionContextStep4) {
        return new JobBuilder("executionContextJob", jobRepository)
                .start(executionContextStep1)
                .next(executionContextStep2)
                .next(executionContextStep3)
                .next(executionContextStep4)
                .build();
    }

    @Bean
    public Step executionContextStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionContextStep1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep2(JobRepository jobRepository, Tasklet executionContextTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionContextStep2", jobRepository)
                .tasklet(executionContextTask2, transactionManager)
                .build();
    }


    @Bean
    public Step executionContextStep3(JobRepository jobRepository, Tasklet executionContextTask3, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionContextStep3", jobRepository)
                .tasklet(executionContextTask3, transactionManager)
                .build();
    }


    @Bean
    public Step executionContextStep4(JobRepository jobRepository, Tasklet executionContextTask4, PlatformTransactionManager transactionManager) {
        return new StepBuilder("executionContextStep4", jobRepository)
                .tasklet(executionContextTask4, transactionManager)
                .build();
    }

    @Bean
    public Tasklet executionContextTask2() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 2 was executed! Using Step Test!");
            log.info("--------------------");
            ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
            ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

            log.info("jobName: {}", jobExecutionContext.getString("jobName"));
            log.info("stepName: {}", stepExecutionContext.get("stepName"));

            String stepName = chunkContext.getStepContext().getStepExecution().getStepName();
            if (!stepExecutionContext.containsKey("stepName")) {
                stepExecutionContext.put("stepName", stepName);
            }

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet executionContextTask3() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 3 was executed! Using Step Test!");
            log.info("--------------------");


            if (!chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().containsKey("name")) {
                chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("name", "chan");
//                throw new RuntimeException("Step 3 was failed!");
            }

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet executionContextTask4() {
        return (contribution, chunkContext) -> {
            log.info("--------------------");
            log.info("Step 4 was executed! Using Step Test!");
            log.info("--------------------");

            log.info("name: {}", chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString("name"));

            return RepeatStatus.FINISHED;
        };
    }
}
