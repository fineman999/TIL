package io.chan.paymentservice.application.inputport;

import io.chan.paymentservice.application.outputport.MemberOutputPort;
import io.chan.paymentservice.application.outputport.OrderOutputPort;
import io.chan.paymentservice.application.outputport.PaymentOutputPort;
import io.chan.paymentservice.application.usecase.SaveOrderUseCase;
import io.chan.paymentservice.domain.Member;
import io.chan.paymentservice.domain.Order;
import io.chan.paymentservice.domain.Payment;
import io.chan.paymentservice.framework.web.dto.OrderInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class SaveOrderInputPort implements SaveOrderUseCase {
    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;
    private final MemberOutputPort memberOutputPort;
    @Override
    public void saveOrder(Long memberId, OrderInputDTO orderInputDTO) {
        Member member = memberOutputPort.getMemberById(memberId);

        Payment payment = Payment.readyToPay(orderInputDTO.price());

        Order order = Order.create(member,
                orderInputDTO.price(),
                orderInputDTO.ItemName(),
                UUID.randomUUID().toString(),
                payment);

        orderOutputPort.saveOrder(order);
    }
}
