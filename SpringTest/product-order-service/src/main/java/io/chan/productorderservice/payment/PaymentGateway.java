package io.chan.productorderservice.payment;

interface PaymentGateway {

    void execute(int price, String cardNumber);
}
