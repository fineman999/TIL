package io.chan.queuingsystemforjava.global.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
    private String secretKey;
    private String baseUrl;
    private String confirmEndpoint;

    // Getter for secretKey
    public String getSecretKey() {
        return secretKey;
    }

    // Setter for secretKey
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    // Getter for baseUrl
    public String getBaseUrl() {
        return baseUrl;
    }

    // Setter for baseUrl
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // Getter for confirmEndpoint
    public String getConfirmEndpoint() {
        return confirmEndpoint;
    }

    // Setter for confirmEndpoint
    public void setConfirmEndpoint(String confirmEndpoint) {
        this.confirmEndpoint = confirmEndpoint;
    }

    public String getConfirmUrl() {
        return baseUrl + confirmEndpoint;
    }
}
