package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payment_discounts")
@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_discounts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentDiscount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "amount")
    private Integer amount;

    public static PaymentDiscount create(Payment payment, Integer amount) {
        return PaymentDiscount.builder()
                .payment(payment)
                .amount(amount)
                .build();
    }
}