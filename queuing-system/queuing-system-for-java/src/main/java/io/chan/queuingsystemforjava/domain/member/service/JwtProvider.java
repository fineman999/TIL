package io.chan.queuingsystemforjava.domain.member.service;


import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;

public interface JwtProvider {
    CustomClaims parseAccessToken(String accessToken);

    String createAccessToken(Member member);
}
