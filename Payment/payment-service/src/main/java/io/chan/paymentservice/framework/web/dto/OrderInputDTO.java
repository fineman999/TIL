package io.chan.paymentservice.framework.web.dto;

import org.springframework.util.Assert;

public record OrderInputDTO(
        Long price,
        String ItemName
) {
    public OrderInputDTO {
        Assert.notNull(price, "Price must not be null");
        Assert.notNull(ItemName, "ItemName must not be null");
    }
}
