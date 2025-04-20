package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.domain.payment.processor.ExternalPaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentConfirmationService;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCreationService;
import io.chan.queuingsystemforjava.domain.payment.service.SimulatedPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class PaymentProcessorConfig {
    @Bean
    public PaymentProcessor externalPaymentProcessor(
            PaymentConfirmationService paymentConfirmationService, PaymentCreationService paymentCreationService) {
        return new ExternalPaymentProcessor(paymentConfirmationService, paymentCreationService);
    }

    @Primary
    @Profile("test")
    @Bean
    public PaymentProcessor testPaymentProcessor() {
        return new SimulatedPaymentProcessor();
    }
}
