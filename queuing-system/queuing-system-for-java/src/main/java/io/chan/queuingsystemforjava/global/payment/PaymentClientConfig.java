package io.chan.queuingsystemforjava.global.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.payment.adapter.PaymentApiClient;
import io.chan.queuingsystemforjava.domain.payment.exception.PaymentExceptionInterceptor;
import io.chan.queuingsystemforjava.global.payment.PaymentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Configuration
public class PaymentClientConfig {
    private static final String BASIC_DELIMITER = ":";
    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

    @Bean
    public RestClient restClient(PaymentProperties paymentProperties, ObjectMapper objectMapper) {
        if (!StringUtils.hasText(paymentProperties.getSecretKey())) {
            throw new IllegalArgumentException("Payment secret key must not be empty");
        }
        if (!StringUtils.hasText(paymentProperties.getBaseUrl())) {
            throw new IllegalArgumentException("Payment base URL must not be empty");
        }

        return RestClient.builder()
                .requestFactory(createRequestFactory())
                .requestInterceptor(new PaymentExceptionInterceptor(objectMapper))
                .defaultHeader(HttpHeaders.AUTHORIZATION, createBasicAuthHeader(paymentProperties.getSecretKey()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(paymentProperties.getBaseUrl())
                .build();
    }

    @Bean
    public PaymentApiClient paymentApiClient(RestClient restClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(PaymentApiClient.class);
    }

    private static String createBasicAuthHeader(String secretKey) {
        String auth = secretKey + BASIC_DELIMITER;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return AUTH_HEADER_PREFIX + new String(encodedAuth, StandardCharsets.UTF_8);
    }

    private static ClientHttpRequestFactory createRequestFactory() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }
}