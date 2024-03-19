package io.chan.springbatch.session12.async;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job writeJob(JobRepository jobRepository, Step syncStep) {
        return new JobBuilder("writeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(syncStep)
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step syncStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("writeStep1", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(pagingItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public Step asyncStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("writeStep1", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(pagingItemReader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    @Bean
    public AsyncItemWriter asyncItemWriter() {
        AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(customItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws InterruptedException {
        AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(customItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return asyncItemProcessor;
    }


    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessor() throws InterruptedException {
        Thread.sleep(1000);
        return item -> new Customer(item.id(), item.firstName().toUpperCase(), item.lastName().toUpperCase(), item.birthDate());
    }

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader() {
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id, firstName, lastName, birthDate");
        provider.setFromClause("from customer");

        Map<String, Order> sortKey = Map.of("id", Order.ASCENDING);
        provider.setSortKeys(sortKey);

        return new JdbcPagingItemReaderBuilder<Customer>()
                .dataSource(dataSource)
                .fetchSize(chunkSize)
                .rowMapper((resultSet, i) -> new Customer(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate()
                ))
                .queryProvider(provider)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> customItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("insert into new_customer values (:id, :firstName, :lastName, :birthDate)")
                .beanMapped()
                .build();
    }
}
