package io.chan.productorderservice.payment.adapter.out.external;

public interface PaymentGateway {

    void execute(int price, String cardNumber);
}
