package io.chan.queuingsystemforjava.domain.payment;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import io.chan.queuingsystemforjava.domain.order.Order;
import io.chan.queuingsystemforjava.global.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payments SET deleted_at = NOW() WHERE payment_key = ?")
@SQLRestriction("deleted_at IS NULL")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_key", nullable = false, unique = true, length = 200)
    private String paymentKey;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "version", length = 10)
    private String version;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "m_id", length = 14)
    private String mId;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "method", length = 50)
    private String method;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "balance_amount", nullable = false)
    private BigDecimal balanceAmount;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "requested_at", nullable = false)
    private ZonedDateTime requestedAt;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(name = "use_escrow", nullable = false)
    private boolean useEscrow;

    @Column(name = "last_transaction_key", length = 64)
    private String lastTransactionKey;

    @Column(name = "supplied_amount")
    private BigDecimal suppliedAmount;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "culture_expense", nullable = false)
    private boolean cultureExpense;

    @Column(name = "tax_free_amount")
    private BigDecimal taxFreeAmount;

    @Column(name = "tax_exemption_amount")
    private Integer taxExemptionAmount;

    @Column(name = "country", length = 2)
    private String country;

    public static Payment create(
            String paymentKey, Order order, String version, String type, String mId, String currency, String method,
            BigDecimal totalAmount, BigDecimal balanceAmount, String status, OffsetDateTime requestedAt,
            OffsetDateTime approvedAt, boolean useEscrow, String lastTransactionKey, BigDecimal suppliedAmount,
            BigDecimal vat, boolean cultureExpense, BigDecimal taxFreeAmount, Integer taxExemptionAmount, String country
    ) {
        return Payment.builder()
                .paymentKey(paymentKey)
                .order(order)
                .version(version)
                .type(type)
                .mId(mId)
                .currency(currency)
                .method(method)
                .totalAmount(totalAmount)
                .balanceAmount(balanceAmount)
                .status(status)
                .requestedAt(DateUtils.toZonedDateTime(requestedAt))
                .approvedAt(DateUtils.toZonedDateTime(approvedAt))
                .useEscrow(useEscrow)
                .lastTransactionKey(lastTransactionKey)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .cultureExpense(cultureExpense)
                .taxFreeAmount(taxFreeAmount)
                .taxExemptionAmount(taxExemptionAmount)
                .country(country)
                .build();
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

}