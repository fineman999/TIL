package io.chan.paymentservice.framework.web.dto;

import io.chan.paymentservice.domain.Order;
import lombok.Builder;

@Builder
public record RequestPayOutputDTO(
        String orderUid,
        String itemName,
        String buyerName,
        Long paymentPrice,
        String buyerEmail,
        String buyerAddress
) {
    public static RequestPayOutputDTO of(Order order) {
        return RequestPayOutputDTO.builder()
                .orderUid(order.getOrderUid())
                .itemName(order.getItemName())
                .buyerName(order.getMember().getName())
                .paymentPrice(order.getPayment().getPrice())
                .buyerEmail(order.getMember().getEmail())
                .buyerAddress(order.getMember().getAddress())
                .build();
    }
}
