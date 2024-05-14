package io.chan.springbatchtestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringBatchTestServiceApplication {

  public static void main(String[] args) {
    SpringApplication.from(SpringBatchTestServiceApplication::main)
        .with(TestSpringBatchTestServiceApplication.class)
        .run(args);
  }
}
