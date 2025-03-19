package io.chan.queuingsystemforjava.domain.performance.service;


import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.performance.dto.PerformanceElement;
import io.chan.queuingsystemforjava.domain.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public ItemResult<PerformanceElement> getPerformances() {
        return ItemResult.of(
                performanceRepository.findAll().stream().map(PerformanceElement::of).toList());
    }

    @Transactional(readOnly = true)
    public PerformanceElement getPerformance(long performanceId) {

        return performanceRepository
                .findById(performanceId)
                .map(PerformanceElement::of)
                .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_PERFORMANCE));
    }
}
