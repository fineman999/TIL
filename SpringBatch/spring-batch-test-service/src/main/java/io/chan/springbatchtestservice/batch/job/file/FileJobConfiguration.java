package io.chan.springbatchtestservice.batch.job.file;

import io.chan.springbatchtestservice.batch.chunk.processor.FileItemProcessor;
import io.chan.springbatchtestservice.batch.domain.Product;
import io.chan.springbatchtestservice.batch.domain.ProductVO;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FileJobConfiguration {
  private static final int CHUNK_SIZE = 10;

  private final EntityManagerFactory entityManagerFactory;
  private final DataSource dataSource;

  @Bean
  public Job fileJob(JobRepository jobRepository, @Qualifier("fileStep") Step fileStep) {
    return new JobBuilder("fileJob", jobRepository).start(fileStep).build();
  }

  @Bean
  public Step fileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("fileStep", jobRepository)
        .<ProductVO, Product>chunk(CHUNK_SIZE, transactionManager)
        .reader(fileItemReader(null))
        .processor(fileItemProcessor())
        .writer(fileItemWriter())
        .build();
  }

  // FlatFileItemReader : 플랫파일(예: txt, csv 등)을 읽어들이는 Reader
  // .fieldSetMapper(new BeanWrapperFieldSetMapper<>()).targetType(ProductVO.class) : 읽어들인 데이터를
  // ProductVO에 매핑
  @StepScope // Job Parameter 또는 Step Parameter를 사용하기 위해 필요
  @Bean
  public FlatFileItemReader<ProductVO> fileItemReader(
          @Value("#{jobParameters[requestDate]}") String requestDate
  ) {
    return new FlatFileItemReaderBuilder<ProductVO>()
        .name("flatFile")
        .resource(new ClassPathResource("product_" + requestDate + ".csv"))
        .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
        .targetType(ProductVO.class)
        .linesToSkip(1)
        .delimited()
        .delimiter(",")
        .names("id","name","price","type")
        .build();
  }

  @Bean
  public ItemProcessor<ProductVO, Product> fileItemProcessor() {
    return new FileItemProcessor();
  }

  @Bean
  public ItemWriter<Product> fileItemWriter() {
    return new JdbcBatchItemWriterBuilder<Product>()
        .sql("INSERT INTO product (name, price, type) VALUES (:name, :price, :type)")
        .dataSource(dataSource)
            .beanMapped()
        .build();
//    return new JpaItemWriterBuilder<Product>()
//        .usePersist(true)
//        .entityManagerFactory(entityManagerFactory)
//        .build();
  }
}
