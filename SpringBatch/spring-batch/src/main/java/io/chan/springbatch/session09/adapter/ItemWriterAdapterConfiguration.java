package io.chan.springbatch.session09.adapter;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ItemWriterAdapterConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job writeJob(JobRepository jobRepository, Step chunkStep1, Step chunkStep2) {
        return new JobBuilder("writeJob", jobRepository)
                .start(chunkStep1)
                .next(chunkStep2)
                .build();
    }

    @Bean
    public Step writeStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("writeStep1", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(new ItemReader<String>() {
                    int i = 0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i ++;
                        return i < 10 ? i + "번째 데이터" : null;
                    }
                })
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public ItemWriter<? super String> customItemWriter() {
        ItemWriterAdapter<String> writerAdapter = new ItemWriterAdapter<>();
        writerAdapter.setTargetObject(customService());
        writerAdapter.setTargetMethod("customWrite");
        return writerAdapter;
    }

    @Bean
    public CustomService<String> customService() {
        return new CustomService<>();
    }
}
