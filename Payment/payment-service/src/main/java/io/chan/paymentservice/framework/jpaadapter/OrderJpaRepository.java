package io.chan.paymentservice.framework.jpaadapter;

import io.chan.paymentservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o " +
            "join fetch o.payment p " +
            "join fetch o.member m where o.orderUid = :orderUId")
    Optional<Order> findOrderAndPaymentAndMemberByOrderUid(String orderUId);

    @Query("select o from Order o " +
            "join fetch o.payment p where o.orderUid = :orderUId")
    Optional<Order> findOrderAndPaymentByOrderUid(String orderUId);
}
