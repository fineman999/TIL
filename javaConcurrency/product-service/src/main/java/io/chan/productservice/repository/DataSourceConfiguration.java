package io.chan.productservice.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource.lock")
    @Bean
    public DataSourceProperties lockDataSourceProperties() {
        return new DataSourceProperties();
    }

    @ConfigurationProperties(prefix = "spring.datasource.lock.hikari")
    @Bean
    public DataSource lockDatasource() {
        return lockDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LockRepository lockRepository() {
        return new LockRepository(lockDatasource());
    }

}
