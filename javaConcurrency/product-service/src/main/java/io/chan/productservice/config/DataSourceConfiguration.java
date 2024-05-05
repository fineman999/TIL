package io.chan.productservice.config;

import com.zaxxer.hikari.HikariDataSource;
import io.chan.productservice.aop.named.NamedLockAspect;
import io.chan.productservice.repository.NamedLockRepository;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration {

  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.main")
  @Bean
  public DataSourceProperties mainDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean
  public DataSource mainDatasource() {
    return mainDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

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
  public NamedLockRepository lockRepository() {
    return new NamedLockRepository(lockDatasource());
  }

  @Bean
  public NamedLockAspect namedLockAspect() {
    return new NamedLockAspect(lockDatasource());
  }
}
