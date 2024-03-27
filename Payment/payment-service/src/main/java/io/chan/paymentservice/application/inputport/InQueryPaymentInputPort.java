package io.chan.paymentservice.application.inputport;

import io.chan.paymentservice.application.outputport.OrderOutputPort;
import io.chan.paymentservice.application.usecase.InQueryPaymentUseCase;
import io.chan.paymentservice.domain.Order;
import io.chan.paymentservice.framework.web.dto.RequestPayOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class InQueryPaymentInputPort implements InQueryPaymentUseCase {
    private final OrderOutputPort orderOutputPort;
    @Override
    public RequestPayOutputDTO inQueryPayment(String orderUid) {
        Order order = orderOutputPort.getOrderAndPaymentAndMemberByOrderUid(orderUid);
        return RequestPayOutputDTO.of(order);
    }

}
