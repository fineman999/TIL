package io.chan.paymentservice.application.usecase;

import io.chan.paymentservice.framework.web.dto.RequestPayOutputDTO;

public interface InQueryPaymentUseCase {
    RequestPayOutputDTO inQueryPayment(String orderUid);
}
