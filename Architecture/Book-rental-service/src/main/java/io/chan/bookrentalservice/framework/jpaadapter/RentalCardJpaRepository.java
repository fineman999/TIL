package io.chan.bookrentalservice.framework.jpaadapter;

import io.chan.bookrentalservice.domain.model.RentalCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalCardJpaRepository extends JpaRepository<RentalCard, String> {
    Optional<RentalCard> findByMemberId(String userId);
}
