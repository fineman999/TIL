package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.domain.payment.processor.ExternalPaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import io.chan.queuingsystemforjava.domain.payment.repository.IdempotencyRedisRepository;
import io.chan.queuingsystemforjava.domain.payment.service.PaymentCancellationService;
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
            PaymentConfirmationService paymentConfirmationService,
            PaymentCreationService paymentCreationService,
            PaymentCancellationService paymentCancellationService,
            IdempotencyRedisRepository idempotencyRedisRepository
            ) {
        return new ExternalPaymentProcessor(paymentConfirmationService, paymentCreationService, paymentCancellationService, idempotencyRedisRepository);
    }

    @Primary
    @Profile("test")
    @Bean
    public PaymentProcessor testPaymentProcessor() {
        return new SimulatedPaymentProcessor();
    }
}
