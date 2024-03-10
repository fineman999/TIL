package io.chan.bookrentalservice.framework.jpaadapter;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.domain.model.RentalCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RentalCardJpaAdapter implements RentalCardOutputPort {

    private final RentalCardJpaRepository rentalCardJpaRepository;
    @Override
    public Optional<RentalCard> loadRentalCard(final String userId) {
        return rentalCardJpaRepository.findByMemberId(userId);
    }

    @Override
    public RentalCard save(final RentalCard rentalCard) {
        return rentalCardJpaRepository.save(rentalCard);
    }
}
