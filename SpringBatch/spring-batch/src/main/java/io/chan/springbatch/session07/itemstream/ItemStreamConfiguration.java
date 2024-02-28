package io.chan.springbatch.session07.itemstream;

import io.chan.springbatch.session07.item.CustomItemProcessor;
import io.chan.springbatch.session07.item.CustomItemReader;
import io.chan.springbatch.session07.item.CustomItemWriter;
import io.chan.springbatch.session07.item.Customer;
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
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStreamConfiguration {

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
                .<String, String>chunk(3, transactionManager)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    private ItemStreamWriter<? super String> itemWriter() {
        return new CustomItemStreamWriter();
    }

    @Bean
    public CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>(20);

        for (int i = 0; i < 20; i++) {
            items.add("Item " + i);
        }

        return new CustomItemStreamReader(items);
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
