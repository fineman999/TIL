package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_easy_pays")
@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_easy_pays SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentEasyPay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    public static PaymentEasyPay create(
        Payment payment, String provider, BigDecimal amount, BigDecimal discountAmount
    ) {
        return PaymentEasyPay.builder()
                .payment(payment)
                .provider(provider)
                .amount(amount)
                .discountAmount(discountAmount)
                .build();
    }
}