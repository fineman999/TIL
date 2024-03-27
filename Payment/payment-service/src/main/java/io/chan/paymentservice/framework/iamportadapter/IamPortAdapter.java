package io.chan.paymentservice.framework.iamportadapter;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.chan.paymentservice.application.outputport.IamPortOutputPort;
import io.chan.paymentservice.config.exception.CustomIOException;
import io.chan.paymentservice.config.exception.CustomIamportException;
import io.chan.paymentservice.framework.web.dto.PaymentCallbackInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class IamPortAdapter implements IamPortOutputPort {
    private final IamportClient iamportClient;

    @Override
    public Payment paymentByImpUid(PaymentCallbackInputDTO paymentCallbackInputDTO){
        try {
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(paymentCallbackInputDTO.paymentUid());
            return iamportResponse.getResponse();
        } catch (IamportResponseException e) {
            throw new CustomIamportException(e.getMessage());
        } catch (IOException e) {
            throw new CustomIOException(e.getMessage());
        }
    }

    @Override
    public Payment cancelPaymentByImpUid(Payment payment){
        try {
            CancelData cancelData = new CancelData(payment.getImpUid(), true, payment.getAmount());
            IamportResponse<Payment> iamportResponse = iamportClient.cancelPaymentByImpUid(cancelData);
            return iamportResponse.getResponse();
        } catch (IamportResponseException e) {
            throw new CustomIamportException(e.getMessage());
        } catch (IOException e) {
            throw new CustomIOException(e.getMessage());
        }
    }

}
