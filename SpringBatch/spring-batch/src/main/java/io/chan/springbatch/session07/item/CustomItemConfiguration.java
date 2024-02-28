package io.chan.springbatch.session07.item;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

public class CustomItemConfiguration {

    @Bean
    public Job chunkJob(JobRepository jobRepository, Step chunkStep1, Step chunkStep2) {
        return new JobBuilder("chunkJob", jobRepository)
                .start(chunkStep1)
                .next(chunkStep2)
                .build();
    }

    @Bean
    public Step chunkStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkStep1", jobRepository)
                .<Customer, Customer>chunk(3, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    private ItemWriter<? super Customer> itemWriter() {
        return new CustomItemWriter();
    }

    @Bean
    public ItemProcessor<? super Customer, ? extends Customer> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemReader<Customer> itemReader() {
        return new CustomItemReader(Arrays.asList(
                new Customer("user1"),
                new Customer("user2"),
                new Customer("user3")
        ));
    }

    @Bean
    public Step chunkStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkStep2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("chunkStep2 was executed!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
