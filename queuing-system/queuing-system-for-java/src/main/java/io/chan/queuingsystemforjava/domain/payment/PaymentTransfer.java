package io.chan.queuingsystemforjava.domain.payment;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payment_transfers")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_transfers SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentTransfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "bank_code", length = 2)
    private String bankCode;

    @Column(name = "settlement_status", length = 50)
    private String settlementStatus;

    public static PaymentTransfer create(
        Payment payment, String bankCode, String settlementStatus
    ) {
        return PaymentTransfer.builder()
                .payment(payment)
                .bankCode(bankCode)
                .settlementStatus(settlementStatus)
                .build();
    }
}