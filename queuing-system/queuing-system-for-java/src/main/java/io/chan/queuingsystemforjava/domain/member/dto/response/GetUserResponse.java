package io.chan.queuingsystemforjava.domain.member.dto.response;

import io.chan.queuingsystemforjava.domain.member.Member;

public record GetUserResponse(
        String email,
        String password
) {
    public static GetUserResponse of(Member member) {
        return new GetUserResponse(member.getEmail(), "test1234");
    }
}
