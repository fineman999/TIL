package io.chan.springbatch.section05.taskletstep;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
public class TaskletStepConfiguration {
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
    public Step taskletStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep1", jobRepository) // StepBuilder를 사용하여 Step을 생성, Step의 이름과 JobRepository를 인자로 받음
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
                        System.out.println("step 1 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Tasklet을 사용하여 Step을 생성, Tasklet과 PlatformTransactionManager를 인자로 받음
                .startLimit(10) // Step의 실행 횟수를 설정, 설정한 만큼 실행되고 초과시 오류 발생, 기본값은 Integer.MAX_VALUE
                .allowStartIfComplete(true) // Step의 성공, 실패와 상관없이 항상 실행할지 여부를 설정, 기본값은 false
                .listener(new StepExecutionListener() { // StepExecutionListener를 사용하여 Step의 실행 전, 후에 실행할 코드를 작성
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        System.out.println("Before Step Execution");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        System.out.println("After Step Execution");
                        return null;
                    }
                })
                .build(); // Step을 생성
    }

    @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testStep2", jobRepository)
                .<String, String>chunk(3, transactionManager) // Chunk를 사용하여 Step을 생성, Chunk의 입력과 출력 타입을 지정, Chunk의 크기와 PlatformTransactionManager를 인자로 받음
                .reader(new ListItemReader<>(Arrays.asList("one", "two", "three")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return item.toUpperCase();
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(final Chunk<? extends String> chunk) throws Exception {
                        for (String item : chunk.getItems()) {
                            System.out.println(">> " + item);
                        }
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
