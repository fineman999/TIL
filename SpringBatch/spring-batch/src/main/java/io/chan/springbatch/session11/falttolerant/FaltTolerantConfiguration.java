package io.chan.springbatch.session11.falttolerant;

import io.chan.springbatch.session10.composite.CustomItemProcessor;
import io.chan.springbatch.session10.composite.CustomItemProcessor2;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FaltTolerantConfiguration {
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
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        if (i == 1) {
                            throw new IllegalAccessException("this exception is skipped");
                        }
                        return i > 3 ? null : i + "번째 데이터";
                    }
                })
                .processor(customItemProcessor())
                .writer(items -> {
                    for (String item : items) {
                        System.out.println(">> " + item);
                    }
                })
                .faultTolerant()
                .skip(IllegalAccessException.class) // skip할 exception을 등록
                .skipLimit(2) // skip할 횟수를 지정
                .retry(IllegalStateException.class) // retry할 exception을 등록
                .retryLimit(2) // retry할 횟수를 지정
                .build();
    }

    @Bean
    public ItemProcessor<? super String, String> customItemProcessor() {
        throw new IllegalStateException("this exception is retried");
    }
}
