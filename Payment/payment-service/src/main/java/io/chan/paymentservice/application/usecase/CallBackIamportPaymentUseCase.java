package io.chan.paymentservice.application.usecase;

import com.siot.IamportRestClient.response.Payment;
import io.chan.paymentservice.framework.web.dto.PaymentCallbackInputDTO;

public interface CallBackIamportPaymentUseCase {
    Payment callbackPayment(PaymentCallbackInputDTO paymentCallbackInputDTO);
}
