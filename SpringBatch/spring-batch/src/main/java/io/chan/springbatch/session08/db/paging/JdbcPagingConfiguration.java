package io.chan.springbatch.session08.db.paging;

import io.chan.springbatch.session08.db.cursor.Customer;
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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Configuration
public class JdbcPagingConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;

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
    public ItemReader<? extends Customer> itemReader() throws Exception {
        Map<String, Object> parameters = Map.of("name", "J%");
        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("jdbcPagingItemReader")
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameters)
                .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("id, name, email, phone, address, created_date, updated_date");
        queryProvider.setFromClause("FROM customer");
        queryProvider.setWhereClause("WHERE name like :name");

        Map<String, Order> sortKeys = Map.of("id", Order.ASCENDING);
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
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
