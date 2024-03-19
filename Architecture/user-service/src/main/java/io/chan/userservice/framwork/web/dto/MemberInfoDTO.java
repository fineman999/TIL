package io.chan.userservice.framwork.web.dto;

public record MemberInfoDTO(
    String id,
    String name,
    String passWord,
    String email,
    long point
) {
}
