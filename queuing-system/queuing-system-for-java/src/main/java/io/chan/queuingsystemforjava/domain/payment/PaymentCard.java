package io.chan.queuingsystemforjava.domain.payment;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_cards")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_cards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "issuer_code", length = 2)
    private String issuerCode;

    @Column(name = "acquirer_code", length = 2)
    private String acquirerCode;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "installment_plan_months")
    private Integer installmentPlanMonths;

    @Column(name = "approve_no", length = 8)
    private String approveNo;

    @Column(name = "use_card_point", nullable = false)
    private boolean useCardPoint;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(name = "owner_type", length = 50)
    private String ownerType;

    @Column(name = "acquire_status", length = 50)
    private String acquireStatus;

    @Column(name = "is_interest_free", nullable = false)
    private boolean isInterestFree;

    @Column(name = "interest_payer", length = 50)
    private String interestPayer;

    public static PaymentCard create(
        Payment payment, BigDecimal amount, String issuerCode, String acquirerCode, String number,
        Integer installmentPlanMonths, String approveNo, boolean useCardPoint, String cardType,
        String ownerType, String acquireStatus, boolean isInterestFree, String interestPayer
    ) {
        return PaymentCard.builder()
                .payment(payment)
                .amount(amount)
                .issuerCode(issuerCode)
                .acquirerCode(acquirerCode)
                .number(number)
                .installmentPlanMonths(installmentPlanMonths)
                .approveNo(approveNo)
                .useCardPoint(useCardPoint)
                .cardType(cardType)
                .ownerType(ownerType)
                .acquireStatus(acquireStatus)
                .isInterestFree(isInterestFree)
                .interestPayer(interestPayer)
                .build();
    }

}