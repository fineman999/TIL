package io.chan.bookrentalservice.framework.web.dto;

public record UserItemInputDTO(
    String userId,
    String userName,
    String itemId,
    String itemTitle
) {
}
