package io.chan.paymentservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String paymentUid;

    public static Payment readyToPay(Long price) {
        return new Payment(null, price, PaymentStatus.READY, null);
    }

    public void changePaymentStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public boolean equalsPrice(BigDecimal amount) {
        return this.price.equals(amount.longValue());
    }

    public void changePaymentBySuccess(com.siot.IamportRestClient.response.Payment payment) {
        this.paymentUid = payment.getImpUid();
        this.status = PaymentStatus.PAID;
    }
}
