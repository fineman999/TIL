package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payment_cash_receipts")
@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_cash_receipts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentCashReceipt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "receipt_key", length = 200)
    private String receiptKey;

    @Column(name = "order_id", length = 255)
    private String orderId;

    @Column(name = "order_name", length = 255)
    private String orderName;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "issue_number", length = 9)
    private String issueNumber;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "business_number", length = 10)
    private String businessNumber;

    @Column(name = "transaction_type", length = 50)
    private String transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "tax_free_amount")
    private BigDecimal taxFreeAmount;

    @Column(name = "issue_status", length = 50)
    private String issueStatus;

    @Column(name = "failure_code", length = 50)
    private String failureCode;

    @Column(name = "failure_message", length = 510)
    private String failureMessage;

    @Column(name = "customer_identity_number", length = 30)
    private String customerIdentityNumber;

    @Column(name = "requested_at")
    private OffsetDateTime requestedAt;

    public static PaymentCashReceipt create(
        Payment payment, String receiptKey, String orderId, String orderName, String type,
        String issueNumber, String receiptUrl, String businessNumber, String transactionType,
        BigDecimal amount, BigDecimal taxFreeAmount, String issueStatus, String failureCode,
        String failureMessage, String customerIdentityNumber, OffsetDateTime requestedAt
    ) {
        return PaymentCashReceipt.builder()
                .payment(payment)
                .receiptKey(receiptKey)
                .orderId(orderId)
                .orderName(orderName)
                .type(type)
                .issueNumber(issueNumber)
                .receiptUrl(receiptUrl)
                .businessNumber(businessNumber)
                .transactionType(transactionType)
                .amount(amount)
                .taxFreeAmount(taxFreeAmount)
                .issueStatus(issueStatus)
                .failureCode(failureCode)
                .failureMessage(failureMessage)
                .customerIdentityNumber(customerIdentityNumber)
                .requestedAt(requestedAt)
                .build();
    }
}