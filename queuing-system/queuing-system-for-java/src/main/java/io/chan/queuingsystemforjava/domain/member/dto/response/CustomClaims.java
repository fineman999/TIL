package io.chan.queuingsystemforjava.domain.member.dto.response;

import io.chan.queuingsystemforjava.domain.member.MemberRole;

public record CustomClaims(
        Long memberId,
        String email,
        MemberRole memberRole
){}
