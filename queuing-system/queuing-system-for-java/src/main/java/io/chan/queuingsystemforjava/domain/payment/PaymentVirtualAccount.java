package io.chan.queuingsystemforjava.domain.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payment_virtual_accounts")
@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment_virtual_accounts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PaymentVirtualAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "account_type", length = 50)
    private String accountType;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "bank_code", length = 2)
    private String bankCode;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "refund_status", length = 50)
    private String refundStatus;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "settlement_status", length = 50)
    private String settlementStatus;

    @Column(name = "refund_bank_code", length = 2)
    private String refundBankCode;

    @Column(name = "refund_account_number", length = 20)
    private String refundAccountNumber;

    @Column(name = "refund_holder_name", length = 100)
    private String refundHolderName;

    @Column(name = "secret", length = 50)
    private String secret;

    public static PaymentVirtualAccount create(
        Payment payment, String accountType, String accountNumber, String bankCode, String customerName,
        String dueDate, String refundStatus, boolean expired, String settlementStatus,
        String refundBankCode, String refundAccountNumber, String refundHolderName, String secret
    ) {
        return PaymentVirtualAccount.builder()
                .payment(payment)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .bankCode(bankCode)
                .customerName(customerName)
                .dueDate(dueDate)
                .refundStatus(refundStatus)
                .expired(expired)
                .settlementStatus(settlementStatus)
                .refundBankCode(refundBankCode)
                .refundAccountNumber(refundAccountNumber)
                .refundHolderName(refundHolderName)
                .secret(secret)
                .build();
    }
}