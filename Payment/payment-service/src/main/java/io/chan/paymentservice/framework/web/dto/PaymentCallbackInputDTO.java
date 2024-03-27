package io.chan.paymentservice.framework.web.dto;

import org.springframework.util.Assert;

public record PaymentCallbackInputDTO (
        String paymentUid,
        String orderUid
){
    public PaymentCallbackInputDTO {
        Assert.hasText(paymentUid, "paymentUid must not be empty");
        Assert.hasText(orderUid, "orderUid must not be empty");
    }
}
