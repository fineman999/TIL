package io.chan.queuingsystemforjava.domain.member.service;


import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;

public interface JwtProvider {
    // Refresh Token 생성
    String createRefreshToken(Member member);

    CustomClaims parseAccessToken(String accessToken);

    String createAccessToken(Member member);

    // Refresh Token 파싱
    String parseRefreshToken(String refreshToken);
}
