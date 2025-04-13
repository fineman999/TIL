package io.chan.queuingsystemforjava.domain.payment;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import io.chan.queuingsystemforjava.global.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payment_cancels")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_cancels SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentCancel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "cancel_amount", nullable = false)
    private BigDecimal cancelAmount;

    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    @Column(name = "tax_free_amount")
    private BigDecimal taxFreeAmount;

    @Column(name = "tax_exemption_amount")
    private Integer taxExemptionAmount;

    @Column(name = "refundable_amount")
    private BigDecimal refundableAmount;

    @Column(name = "transfer_discount_amount")
    private BigDecimal transferDiscountAmount;

    @Column(name = "easy_pay_discount_amount")
    private BigDecimal easyPayDiscountAmount;

    @Column(name = "canceled_at", nullable = false)
    private ZonedDateTime canceledAt;

    @Column(name = "transaction_key", length = 64)
    private String transactionKey;

    @Column(name = "receipt_key", length = 200)
    private String receiptKey;

    @Column(name = "cancel_status", length = 50)
    private String cancelStatus;

    @Column(name = "cancel_request_id", length = 200)
    private String cancelRequestId;

    @Column(name = "is_partial_cancelable", nullable = false)
    private boolean isPartialCancelable;

    public static PaymentCancel create(
        Payment payment, BigDecimal cancelAmount, String cancelReason, BigDecimal taxFreeAmount,
        Integer taxExemptionAmount, BigDecimal refundableAmount, BigDecimal transferDiscountAmount,
        BigDecimal easyPayDiscountAmount, OffsetDateTime canceledAt, String transactionKey,
        String receiptKey, String cancelStatus, String cancelRequestId, boolean isPartialCancelable
    ) {
        return PaymentCancel.builder()
                .payment(payment)
                .cancelAmount(cancelAmount)
                .cancelReason(cancelReason)
                .taxFreeAmount(taxFreeAmount)
                .taxExemptionAmount(taxExemptionAmount)
                .refundableAmount(refundableAmount)
                .transferDiscountAmount(transferDiscountAmount)
                .easyPayDiscountAmount(easyPayDiscountAmount)
                .canceledAt(DateUtils.toZonedDateTime(canceledAt))
                .transactionKey(transactionKey)
                .receiptKey(receiptKey)
                .cancelStatus(cancelStatus)
                .cancelRequestId(cancelRequestId)
                .isPartialCancelable(isPartialCancelable)
                .build();
    }
}