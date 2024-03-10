package io.chan.bookrentalservice.framework.web.dto;

public record ClearOverdueInfoDto(
    String userId,
    String username,
    Long point
) {
}
