package io.chan.queuingsystemforjava.support.mock;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;

public class MockJwtProvider implements JwtProvider {
    private final String issuer;
    private final int expirationSeconds;
    private final String secretKey;

    // 고정된 토큰 값 (테스트용)
    private static final String ADMIN_TOKEN = "mocked-jwt-token-for-admin";
    private static final String USER_TOKEN = "mocked-jwt-token-for-member";

    public MockJwtProvider(String issuer, int expirationSeconds, String secretKey) {
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
        this.secretKey = secretKey;
    }

    @Override
    public String createRefreshToken(final Member member) {
        return "";
    }

    @Override
    public CustomClaims parseAccessToken(String accessToken) {
        if (ADMIN_TOKEN.equals(accessToken)) {
            return new CustomClaims(1L, "admin@gmail.com", MemberRole.ADMIN);
        } else if (USER_TOKEN.equals(accessToken)) {
            return new CustomClaims(2L, "user@gmail.com", MemberRole.USER);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    @Override
    public String createAccessToken(Member member) {
        if (member.getMemberRole() == MemberRole.ADMIN) {
            return ADMIN_TOKEN;
        } else if (member.getMemberRole() == MemberRole.USER) {
            return USER_TOKEN;
        } else {
            throw new IllegalArgumentException("Unsupported role: " + member.getMemberRole());
        }
    }

    @Override
    public String parseRefreshToken(final String refreshToken) {
        return "";
    }
}
