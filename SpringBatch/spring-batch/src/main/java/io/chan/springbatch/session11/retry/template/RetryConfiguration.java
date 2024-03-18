package io.chan.springbatch.session11.retry.template;

import io.chan.springbatch.session11.retry.RetryableException;
import io.chan.springbatch.session11.skip.SkipItemWriter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job retryJob2(JobRepository jobRepository, Step chunkStep1, Step chunkStep2) {
        return new JobBuilder("retryJob2", jobRepository)
                .start(chunkStep1)
                .next(chunkStep2)
                .build();
    }

    @Bean
    public Step writeStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("writeStep1", jobRepository)
                .<String, Customer>chunk(chunkSize, transactionManager)
                .reader(reader())
                .processor(customItemProcessor())
                .writer(item -> {
                    for (Customer customer : item) {
                        System.out.println(">> " + customer);
                    }
                })
                .faultTolerant()
                .skip(RetryableException.class) // retry는 skip하는 기능이 없으므로 재시도할 만큼의 skip을 지정해야 함
                .skipLimit(2) // skip할 횟수를 지정
//                .retry(RetryableException.class) // 재시도할 exception을 등록
//                .retryLimit(2) // 재시도 횟수를 지정
//                .retryPolicy(retryPolicy())
                .build();
    }

    @Bean
    public ItemReader<String> reader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(i + "번째 데이터");
        }
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemProcessor<? super String, Customer> customItemProcessor() {
        return new RetryItemProcessor2(retryTemplate());
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(RetryableException.class, true);
        return new SimpleRetryPolicy(2, exceptionMap);
    }

    @Bean
    public RetryTemplate retryTemplate() {
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000);

        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(RetryableException.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(2, exceptionMap);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }
}
