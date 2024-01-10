package io.chan.productorderservice.payment.adapter;

interface PaymentGateway {

    void execute(int price, String cardNumber);
}
