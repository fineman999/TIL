package io.chan.paymentservice.application.outputport;

import com.siot.IamportRestClient.response.Payment;
import io.chan.paymentservice.framework.web.dto.PaymentCallbackInputDTO;

public interface IamPortOutputPort {

    Payment paymentByImpUid(PaymentCallbackInputDTO paymentCallbackInputDTO);

    Payment cancelPaymentByImpUid(Payment payment);
}
