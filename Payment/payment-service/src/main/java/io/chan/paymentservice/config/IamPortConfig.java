package io.chan.paymentservice.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamPortConfig {
    private final String apiKey;
    private final String apiSecret;

    public IamPortConfig(
            @Value("${iamport.api.key}")
            String apiKey,
            @Value("${iamport.api.secret}")
            String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
