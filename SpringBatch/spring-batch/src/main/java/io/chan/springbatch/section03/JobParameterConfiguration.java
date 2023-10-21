package io.chan.springbatch.section03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@Configuration
public class JobParameterConfiguration {

    @Bean
    public Job parameterJob(JobRepository jobRepository, Step parameterStep1, Step parameterStep2) {
        return new JobBuilder("parameterJob", jobRepository)
                .start(parameterStep1)
                .next(parameterStep2)
                .build();
    }

    @Bean
    public Step parameterStep1(JobRepository jobRepository, Tasklet parameterTask1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("parameterStep1", jobRepository)
                .tasklet(parameterTask1, transactionManager)
                .build();
    }

    @Bean
    public Step parameterStep2(JobRepository jobRepository, Tasklet parameterTask2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("parameterStep2", jobRepository)
                .tasklet(parameterTask2, transactionManager)
                .build();
    }

    @Bean
    public Tasklet parameterTask1() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Hello, World! Using Job Parameter!");

            JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
            log.info("name: {}", jobParameters.getString("name"));
            log.info("seq: {}", jobParameters.getLong("seq"));
            log.info("date: {}", jobParameters.getDate("date"));
            log.info("double: {}", jobParameters.getDouble("double"));
            log.info("localDateTime: {}", jobParameters.getLocalDateTime("localDateTime"));

            // 값 확인 용
            Map<String, Object> chunkContextJobParameters = chunkContext.getStepContext().getJobParameters();
            log.info("chunkContextJobParameters: {}", chunkContextJobParameters);

            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet parameterTask2() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Step 2 was executed! Using Job Parameter!");
            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
