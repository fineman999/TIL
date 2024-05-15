package io.chan.springbatchtestservice;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration // Spring Boot의 자동 설정을 활성화한다.
@EnableBatchProcessing // Spring Batch의 기능을 활성화한다. 이를 통해 배치 작업을 위한 기본 설정이 제공됨.
public class TestBatchConfig {}
