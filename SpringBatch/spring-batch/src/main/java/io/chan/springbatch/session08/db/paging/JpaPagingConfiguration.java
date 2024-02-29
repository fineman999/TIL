package io.chan.springbatch.session08.db.paging;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JpaPagingConfiguration {
    private final int chunkSize = 10;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job chunkJob(JobRepository jobRepository, Step chunkStep1, Step chunkStep2) {
        return new JobBuilder("chunkJob", jobRepository)
                .start(chunkStep1)
                .next(chunkStep2)
                .build();
    }

    @Bean
    public Step chunkStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("chunkStep1", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(itemReader())
                .writer(chunk -> {
                    for (Customer item : chunk.getItems()) {
                        System.out.println(">> " + item);
                    }
                })
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> itemReader() {
        Map<String, Object> parameters = Map.of("name", "J%");
        return new JpaPagingItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT c FROM Customer c JOIN FETCH c.address WHERE c.name like :name")
                .parameterValues(parameters)
                .build();
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
