package io.chan.paymentservice.application.inputport;

import com.siot.IamportRestClient.response.Payment;
import io.chan.paymentservice.application.outputport.IamPortOutputPort;
import io.chan.paymentservice.application.outputport.OrderOutputPort;
import io.chan.paymentservice.application.usecase.CallBackIamportPaymentUseCase;
import io.chan.paymentservice.config.exception.CustomIamportException;
import io.chan.paymentservice.domain.Order;
import io.chan.paymentservice.domain.PaymentStatus;
import io.chan.paymentservice.framework.web.dto.PaymentCallbackInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CallBackIamportPaymentInputPort implements CallBackIamportPaymentUseCase {
    private final IamPortOutputPort iamPortOutputPort;
    private final OrderOutputPort orderOutputPort;

    @Override
    public Payment callbackPayment(PaymentCallbackInputDTO paymentCallbackInputDTO) {

        Payment payment = iamPortOutputPort.paymentByImpUid(paymentCallbackInputDTO);
        Order order = orderOutputPort.getOrderAndPayment(paymentCallbackInputDTO.orderUid());

        if (!payment.getStatus().equals("paid")) {
            order.getPayment().changePaymentStatus(PaymentStatus.FAIL);
            throw new CustomIamportException("Payment status is not paid");
        }

        if(!order.getPayment().equalsPrice(payment.getAmount())) {
            order.getPayment().changePaymentStatus(PaymentStatus.FAIL);
            iamPortOutputPort.cancelPaymentByImpUid(payment);
            throw new CustomIamportException("Payment price is not equal");
        }

        order.getPayment().changePaymentBySuccess(payment);
        return payment;
    }
}
