package io.chan.springbatch.session09.json;

import io.chan.springbatch.session09.flatfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JsonConfiguration {
    private final int chunkSize = 10;
    private final DataSource dataSource;

    public JsonConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super Customer> customItemWriter() {
       return new JsonFileItemWriterBuilder<Customer>()
               .name("customItemWriter")
                .resource(new FileSystemResource("output/customer.json"))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .build();
    }



    @Bean
    public ItemReader<? extends Customer> customItemReader() {
        JdbcPagingItemReader<Customer> itemReader = new JdbcPagingItemReader<>();

        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(chunkSize);
        itemReader.setRowMapper((resultSet, i) -> new Customer(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age")
        ));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, age");
        queryProvider.setFromClause("from customer");
        queryProvider.setWhereClause("where age >= 40");

        HashMap<String, Order> hashMap = new HashMap<>(1);

        hashMap.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(hashMap);
        itemReader.setQueryProvider(queryProvider);

        return itemReader;
    }
}
