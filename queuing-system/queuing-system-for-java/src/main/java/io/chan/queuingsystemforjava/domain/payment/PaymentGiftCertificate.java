package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payment_gift_certificates")
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_gift_certificates SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentGiftCertificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "approve_no", length = 8)
    private String approveNo;

    @Column(name = "settlement_status", length = 50)
    private String settlementStatus;

    public static PaymentGiftCertificate create(
        Payment payment, String approveNo, String settlementStatus
    ) {
        return PaymentGiftCertificate.builder()
                .payment(payment)
                .approveNo(approveNo)
                .settlementStatus(settlementStatus)
                .build();
    }
}