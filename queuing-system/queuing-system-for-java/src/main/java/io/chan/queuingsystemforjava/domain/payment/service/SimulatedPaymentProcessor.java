package io.chan.queuingsystemforjava.domain.payment.service;

import io.chan.queuingsystemforjava.domain.payment.dto.PaymentRequest;
import io.chan.queuingsystemforjava.domain.payment.processor.PaymentProcessor;
import java.util.Random;

public class SimulatedPaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(PaymentRequest paymentRequest) {
        Random random = new Random();

        // 80%의 확률로 2초 내에 결제가 수행되고, 20% 확률로 2초 ~ 5초 사이에 결제 수행
        simulateRandomDelay(random);

        // 5% 확률로 결제 실패 예외 발생
        //        if (random.nextInt(100) < 5) {
        //            throw new TicketingException(ErrorCode.PAYMENT_FAILED);
        //        }
    }

    private void simulateRandomDelay(Random random) {
        int delay;
        if (random.nextInt(100) < 80) {
            delay = 500 + random.nextInt(1500);
        } else {
            delay = 2000 + random.nextInt(6000);
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        }
    }
}