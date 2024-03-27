package io.chan.paymentservice.application.usecase;

import io.chan.paymentservice.framework.web.dto.OrderInputDTO;

public interface SaveOrderUseCase {
    void saveOrder(Long memberId, OrderInputDTO orderInputDTO);
}
