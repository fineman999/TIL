package io.chan.springbatch.session08.flatfile;

import io.chan.springbatch.session07.itemstream.CustomItemStreamReader;
import io.chan.springbatch.session07.itemstream.CustomItemStreamWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

public class FlatFilesConfiguration {

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
                .<Customer, Customer>chunk(5, transactionManager)
                .reader(itemReader())
                .writer(chunk -> {
                    for (Customer item : chunk.getItems()) {
                        System.out.println(">> " + item);
                    }
                })
                .build();
    }

//    @Bean
//    public ItemReader<? extends Customer> itemReader() {
//        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new ClassPathResource("/customers.txt"));
//
//        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
//        lineMapper.setFieldSetMapper(new CustomerFiledSetMapper());
//
//        itemReader.setLineMapper(lineMapper);
//        itemReader.setLinesToSkip(1);
//        return itemReader;
//
//    }

    // FlatFileItemReader 빌더를 사용하여 ItemReader 빈을 생성
//    @Bean
//    public ItemReader<? extends Customer> itemReader() {
//       return new FlatFileItemReaderBuilder<Customer>()
//               .name("flatFileItemReader")
//               .resource(new ClassPathResource("/customers.txt"))
//               .fieldSetMapper(new BeanWrapperFieldSetMapper<>()).targetType(Customer.class) // BeanWrapperFieldSetMapper 를 사용하여 필드 매핑
//               .linesToSkip(1)
//               .delimited().delimiter(",") // 구분자 설정
//               .names("name", "age", "year")
//               .build();
//    }

    @Bean
    public ItemReader<? extends Customer> itemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("flatFileItemReader")
                .resource(new FileSystemResource("src/main/resources/customer.txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(Customer.class)
                .linesToSkip(1)
                .fixedLength()
                .strict(false) // 열 길이가 다를 경우 예외를 던지지 않고 무시
                .addColumns(new Range(1, 5)) // name 길이 설정
                .addColumns(new Range(6, 9)) // year 길이 설정
                .addColumns(new Range(10, 11)) // age 길이 설정
                .names("name", "year", "age")
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
