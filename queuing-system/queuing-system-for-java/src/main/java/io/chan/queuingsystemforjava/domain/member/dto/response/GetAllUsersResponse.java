package io.chan.queuingsystemforjava.domain.member.dto.response;

import io.chan.queuingsystemforjava.domain.member.Member;

import java.util.List;

public record GetAllUsersResponse(
        List<GetUserResponse> userPayloads
) {
    public static GetAllUsersResponse of(List<Member> members) {
        return new GetAllUsersResponse(
                members.stream()
                        .map(GetUserResponse::of)
                        .toList()
        );
    }
}
