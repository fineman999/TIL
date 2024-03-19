 package io.chan.springbatch.session12.parallel;

 import io.chan.springbatch.session12.StopWatchJobListener;
 import jakarta.persistence.EntityManagerFactory;
 import lombok.RequiredArgsConstructor;
 import org.springframework.batch.core.Job;
 import org.springframework.batch.core.job.builder.FlowBuilder;
 import org.springframework.batch.core.job.builder.JobBuilder;
 import org.springframework.batch.core.job.flow.Flow;
 import org.springframework.batch.core.job.flow.support.SimpleFlow;
 import org.springframework.batch.core.launch.support.RunIdIncrementer;
 import org.springframework.batch.core.repository.JobRepository;
 import org.springframework.batch.core.step.builder.StepBuilder;
 import org.springframework.batch.core.step.tasklet.Tasklet;
 import org.springframework.batch.core.step.tasklet.TaskletStep;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.core.task.TaskExecutor;
 import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
 import org.springframework.transaction.PlatformTransactionManager;

 import javax.sql.DataSource;

 @Configuration
@RequiredArgsConstructor
public class ParallelStepConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job writeJob(JobRepository jobRepository, Flow flow1, Flow flow2) {
        return new JobBuilder("writeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flow1)
                .split(taskExecutor()).add(flow2)
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Flow flow1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        TaskletStep step = new StepBuilder("step1", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(step)
                .build();
    }

    @Bean
    public Flow flow2(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        TaskletStep step = new StepBuilder("step2", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();
        TaskletStep step2 = new StepBuilder("step3", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(step)
                .next(step2)
                .build();
    }



    // 모두 동일한 tasklet을 사용
    @Bean
     public Tasklet tasklet() {
        return new CustomTasklet();
     }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("spring_batch");
        return taskExecutor;
    }
}
