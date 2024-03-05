package io.chan.springbatch.session10.classifier;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CompositionItemConfiguration {
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
                .<ProcessorInfo, ProcessorInfo>chunk(chunkSize, transactionManager)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public ProcessorInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        ProcessorInfo processorInfo = ProcessorInfo.builder().id(i).build();
                        return i > 3 ? null : processorInfo;
                    }
                })
                .processor(customItemProcessor())
                .writer(items -> System.out.println(">> " + items))
                .build();
    }

    @Bean
    public ItemProcessor<? super ProcessorInfo, ? extends ProcessorInfo> customItemProcessor() {
        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();
        Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
        processorMap.put(1, new CustomItemProcessor1());
        processorMap.put(2, new CustomItemProcessor2());
        processorMap.put(3, new CustomItemProcessor3());
        classifier.setProcessorMap(processorMap);

        processor.setClassifier(classifier);

        return processor;
    }

}
