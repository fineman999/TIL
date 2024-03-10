package io.chan.bookrentalservice.framework.web.dto;

import org.springframework.util.Assert;

public record UserInputDTO(
    String id,
    String name
) {

    public UserInputDTO {
        Assert.hasText(id, "id must not be empty");
        Assert.hasText(name, "name must not be empty");
    }
}
