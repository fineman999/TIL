package io.chan.springbatch.session08.db;

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
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterConfiguration {
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
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(itemReader())
                .writer(chunk -> {
                    for (String item : chunk.getItems()) {
                        System.out.println(">> " + item);
                    }
                })
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        final ItemReaderAdapter<String> readerAdapter = new ItemReaderAdapter<>();
        readerAdapter.setTargetObject(customerService());
        readerAdapter.setTargetMethod("customRead");
        return readerAdapter;
    }

    @Bean
    public Object customerService() {
        return new CustomService<String>();
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
