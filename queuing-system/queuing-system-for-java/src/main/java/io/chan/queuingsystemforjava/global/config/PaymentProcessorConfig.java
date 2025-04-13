package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.domain.payment.processor.ExternalPaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentPersistenceService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentService;
import io.chan.queuingsystemforjava.domain.payment.service.SimulatedPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class PaymentProcessorConfig {
    @Bean
    public PaymentProcessor externalPaymentProcessor(
            PaymentService paymentService, PaymentPersistenceService paymentPersistenceService) {
        return new ExternalPaymentProcessor(paymentService, paymentPersistenceService);
    }

    @Primary
    @Profile("test")
    @Bean
    public PaymentProcessor testPaymentProcessor() {
        return new SimulatedPaymentProcessor();
    }
}
