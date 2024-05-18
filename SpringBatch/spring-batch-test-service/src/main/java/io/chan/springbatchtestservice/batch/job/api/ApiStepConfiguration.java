package io.chan.springbatchtestservice.batch.job.api;

import io.chan.springbatchtestservice.batch.chunk.processor.ApiItemProcessorA;
import io.chan.springbatchtestservice.batch.chunk.processor.ApiItemProcessorB;
import io.chan.springbatchtestservice.batch.chunk.processor.ApiItemProcessorC;
import io.chan.springbatchtestservice.batch.chunk.writer.ApiItemWriterA;
import io.chan.springbatchtestservice.batch.chunk.writer.ApiItemWriterB;
import io.chan.springbatchtestservice.batch.chunk.writer.ApiItemWriterC;
import io.chan.springbatchtestservice.batch.classifier.ProcessorClassifier;
import io.chan.springbatchtestservice.batch.classifier.WriterClassifier;
import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import io.chan.springbatchtestservice.batch.domain.ProductVO;
import io.chan.springbatchtestservice.batch.partition.ProductPartitioner;
import io.chan.springbatchtestservice.service.ApiServiceA;
import io.chan.springbatchtestservice.service.ApiServiceB;
import io.chan.springbatchtestservice.service.ApiServiceC;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {
  private final DataSource dataSource;
  private static final int chunkSize = 10;
  private final ApiServiceA apiServiceA;
  private final ApiServiceB apiServiceB;
  private final ApiServiceC apiServiceC;

  @Bean
  public Step apiMasterStep(
      JobRepository jobRepository, @Qualifier("apiSlaveStep") Step apiSlaveStep) {
    return new StepBuilder("apiMasterStep", jobRepository)
        .partitioner(apiSlaveStep.getName(), partitioner()) // partitioner()를 통해 Step을 여러개로 나누어 실행
        .step(apiSlaveStep) // partitioner()로 나눈 Step을 실행
        .gridSize(3) // gridSize만큼 Step을 병렬로 실행
        .taskExecutor(taskExecutor()) // 병렬로 실행할 Step을 처리할 TaskExecutor 멀티 스레드로 실행
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(3);
    taskExecutor.setMaxPoolSize(6);
    taskExecutor.setThreadNamePrefix("api-partition-thread");
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true); // 종료 시 처리중인 작업을 마무리하고 종료
    //    taskExecutor.initialize(); // ThreadPoolTaskExecutor는 초기화를 해주어야 사용할 수 있다. - 빈으로 등록시 자동으로
    // 호출됨
    return taskExecutor;
  }

  @Bean
  public Step apiSlaveStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
    return new StepBuilder("apiSlaveStep", jobRepository)
        .<ProductVO, ApiRequestVO>chunk(chunkSize, transactionManager)
        .reader(itemReader(null))
        .processor(itemProcessor())
        .writer(itemWriter())
        .build();
  }

  @StepScope
  @Bean
  public ItemProcessor<ProductVO, ApiRequestVO> itemProcessor() {
    ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor =
        new ClassifierCompositeItemProcessor<>();

    Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap =
        Map.of(
            "1", new ApiItemProcessorA(),
            "2", new ApiItemProcessorB(),
            "3", new ApiItemProcessorC());
    ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier =
        new ProcessorClassifier<>(processorMap);
    processor.setClassifier(classifier);
    return processor;
  }

  @Bean
  public ItemWriter<? super ApiRequestVO> itemWriter() {
    ClassifierCompositeItemWriter<ApiRequestVO> writer = new ClassifierCompositeItemWriter<>();
    Map<String, ItemWriter<ApiRequestVO>> writerMap =
        Map.of(
            "1", new ApiItemWriterA(apiServiceA),
            "2", new ApiItemWriterB(apiServiceB),
            "3", new ApiItemWriterC(apiServiceC));
    WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier =
        new WriterClassifier<>(writerMap);
    writer.setClassifier(classifier);
    return writer;
  }

  @StepScope
  @Bean
  public ItemReader<ProductVO> itemReader(
      @Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {
    JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

    reader.setDataSource(dataSource);
    reader.setPageSize(chunkSize);
    reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("id, name, price, type");
    queryProvider.setFromClause("from product");
    queryProvider.setWhereClause("where type = :type");

    Map<String, Order> sortKeys = new HashMap<>(1);
    sortKeys.put("id", Order.DESCENDING);
    queryProvider.setSortKeys(sortKeys);

    reader.setParameterValues(QueryGenerator.getParameterValues("type", productVO.getType()));
    reader.setQueryProvider(queryProvider);
    reader.afterPropertiesSet();
    return reader;
  }

  @Bean
  public Partitioner partitioner() {
    return new ProductPartitioner(dataSource);
  }
}
