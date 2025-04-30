package io.chan.queuingsystemforjava.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.exception.PaymentExceptionInterceptor;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.processor.SimulatedPaymentProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@TestConfiguration
public class TestConfig {

    @Bean
    public PaymentApiClient paymentApiClient(RestClient restClient) {
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(PaymentApiClient.class);
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder, ObjectMapper objectMapper) {
        // Use the builder injected by Spring Boot Test
        return builder
                .baseUrl("https://api.tosspayments.com")
                .requestInterceptor(new PaymentExceptionInterceptor(objectMapper))
                .build();
    }

    @Bean
    public PaymentProcessor paymentProcessor() {
        return new SimulatedPaymentProcessor();
    }
}
