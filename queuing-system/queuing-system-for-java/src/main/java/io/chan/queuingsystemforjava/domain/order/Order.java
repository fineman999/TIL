package io.chan.queuingsystemforjava.domain.order;

import io.chan.queuingsystemforjava.common.entity.BaseEntity;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.performance.Performance;
import io.chan.queuingsystemforjava.domain.seat.Seat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

/**
 order_id VARCHAR(255) NOT NULL, -- 고유 주문 ID
 performance_id VARCHAR(255) NOT NULL, -- 공연 ID
 seat_id VARCHAR(255) NOT NULL, -- 좌석 ID
 amount DECIMAL(10, 2) NOT NULL, -- 결제 금액 (KRW, 소수점 2자리)
 customer_email VARCHAR(255), -- 고객 이메일
 customer_name VARCHAR(100), -- 고객 이름
 customer_mobile_phone VARCHAR(20), -- 고객 전화번호
 order_name VARCHAR(255) NOT NULL, -- 주문 이름 (예: 공연 티켓 - 좌석 A-12)
 status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') DEFAULT 'PENDING', -- 주문 상태
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성 시간
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 업데이트 시간
 PRIMARY KEY (order_id),
 FOREIGN KEY (performance_id) REFERENCES performances(id) ON DELETE RESTRICT,
 FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE RESTRICT,
 INDEX idx_status (status), -- 상태별 조회를 위한 인덱스
 INDEX idx_created_at (created_at) -- 시간별 조회를 위한 인덱스
 */
@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE orders SET deleted_at = NOW() WHERE order_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, insertable = false, updatable = false)
    private Seat seat;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_mobile_phone")
    private String customerMobilePhone;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    public static Order create(final String orderId, final Performance performance, final Seat seat, final BigDecimal amount,
                               final String customerEmail, final String customerName, final String customerMobilePhone,
                               final String orderName, final OrderStatus status, final Member member) {
        return Order.builder()
                .orderId(orderId)
                .performance(performance)
                .seat(seat)
                .seatId(seat.getSeatId())
                .amount(amount)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .customerMobilePhone(customerMobilePhone)
                .orderName(orderName)
                .status(status)
                .member(member)
                .build();
    }

    public void markAsCompleted() {
        if (!status.isPending()) {
            throw new IllegalStateException("Order is not in a state that can be completed.");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void markAsCancelled() {
        if (!status.isCompleted()) {
            throw new IllegalStateException("Order is not in a state that can be cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
