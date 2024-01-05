package io.chan.productorderservice.payment;

class ConsolepaymentGateway implements PaymentGateway {
    @Override
    public void execute(final int price, final String cardNumber) {
        System.out.println("결제가 완료되었습니다.");
    }
}
