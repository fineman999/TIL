package io.chan.productorderservice.payment.adapter;

import org.springframework.stereotype.Component;

@Component
class ConsolepaymentGateway implements PaymentGateway {
    @Override
    public void execute(final int price, final String cardNumber) {
        System.out.println("결제가 완료되었습니다.");
    }
}
