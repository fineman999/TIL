package io.chan.queuingsystemforjava.domain.order.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.Order;

import java.time.LocalDate;
import java.util.Optional;

import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    @Query("""
            SELECT o FROM Order o
            JOIN FETCH o.performance p
            WHERE o.member = :member
            AND (:startDate IS NULL OR o.createdAt >= :startDate)
            AND (:endDate IS NULL OR o.createdAt <= :endDate)
            AND (:orderName IS NULL OR o.orderName LIKE %:orderName%)
            AND (:status IS NULL OR o.status = :status)
            ORDER BY o.createdAt DESC
            """)
    Page<Order> findOrdersByMember(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("orderName") String orderName,
            @Param("status") OrderStatus status,
            Pageable pageable);

    @Query("""
        SELECT o FROM Order o
        JOIN FETCH o.performance p
        WHERE o.orderId = :orderId
        """)
    Optional<Order> findByOrderIdWithDetails(@Param("orderId") String orderId);

    @Query("SELECT o FROM Order as o WHERE o.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findByIdWithPessimistic(Long id);

    @Query("SELECT o FROM Order as o WHERE o.orderId = :orderId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findByOrderIdWithPessimistic(String orderId);
}