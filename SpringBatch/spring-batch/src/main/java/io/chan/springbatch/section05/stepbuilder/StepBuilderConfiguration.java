package io.chan.springbatch.section05.stepbuilder;

import io.chan.springbatch.section04.CustomJobParametersIncrementer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepBuilderConfiguration {
    @Bean
    public Job stepBuilderJob(JobRepository jobRepository, Step testStep1, Step testStep2, Step testStep3) {
        return new JobBuilder("stepBuilderJob", jobRepository)
                .start(testStep1)
                .next(testStep2)
                .next(testStep3)
                .incrementer(new RunIdIncrementer())
                .validator(new DefaultJobParametersValidator(new String[]{"name", "date"}, new String[]{"count"}))
                .build();
    }

    @Bean
    public Step taskletStep1(JobRepository jobRepository, Tasklet testTask1, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep1", jobRepository)
                .tasklet(testTask1, transactionManager)
                .build();
    }

    @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep2", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(final Chunk<? extends String> chunk) throws Exception {

                    }
                })
                .build();
    }

    @Bean
    public Step partitionerStep3(JobRepository jobRepository, Step testStep2) {
        return new StepBuilder("testStep3", jobRepository)
                .partitioner(testStep2)
                .build();
    }

    // job을 호출함
    @Bean
    public Step jobStep4(JobRepository jobRepository,Job stepBuilderJob) {
        return new StepBuilder("testStep4", jobRepository)
                .job(stepBuilderJob)
                .build();
    }

    @Bean
    public Step flowStep5(JobRepository jobRepository, Flow flow1) {
        return new StepBuilder("testStep5", jobRepository)
                .flow(flow1)
                .build();
    }

    @Bean
    public Flow flow1(Step testStep1, Step testStep2) {
        return new FlowBuilder<Flow>("flow1")
                .start(testStep1)
                .next(testStep2)
                .build();
    }


    @Bean
    public Tasklet testTask1() {
        return (contribution, chunkContext) -> {
            System.out.println("--------------------");
            System.out.println("Hello, World!");
            System.out.println("--------------------");
            return RepeatStatus.FINISHED;
        };
    }
}
