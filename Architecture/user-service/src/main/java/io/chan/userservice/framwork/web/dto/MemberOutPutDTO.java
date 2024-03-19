package io.chan.userservice.framwork.web.dto;

import io.chan.userservice.domain.model.Member;

public record MemberOutPutDTO(
    String id,
    String name,
    String passWord,
    String email
) {
    public static MemberOutPutDTO from(final Member member) {
        return new MemberOutPutDTO(
            member.getIdName().getId(),
            member.getIdName().getName(),
            member.getPassword().getValue(),
            member.getEmail().getValue()
        );
    }
}
