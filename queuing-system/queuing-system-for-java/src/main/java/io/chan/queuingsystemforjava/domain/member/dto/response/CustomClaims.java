package io.chan.queuingsystemforjava.domain.member.dto.response;

import io.chan.queuingsystemforjava.domain.member.MemberRole;

public record CustomClaims(
        String email,
        MemberRole memberRole
){}
