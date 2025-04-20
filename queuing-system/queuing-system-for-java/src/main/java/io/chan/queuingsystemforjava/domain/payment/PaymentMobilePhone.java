package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payment_mobile_phones")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_mobile_phones SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentMobilePhone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "customer_mobile_phone", length = 15)
    private String customerMobilePhone;

    @Column(name = "settlement_status", length = 50)
    private String settlementStatus;

    @Column(name = "receipt_url")
    private String receiptUrl;

    public static PaymentMobilePhone create(
        Payment payment, String customerMobilePhone, String settlementStatus, String receiptUrl
    ) {
        return PaymentMobilePhone.builder()
                .payment(payment)
                .customerMobilePhone(customerMobilePhone)
                .settlementStatus(settlementStatus)
                .receiptUrl(receiptUrl)
                .build();
    }
}