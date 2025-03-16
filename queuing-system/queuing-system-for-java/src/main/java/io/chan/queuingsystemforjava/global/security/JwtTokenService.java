package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.global.cache.RedisRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token:"; // Redis 키 접두사
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7); // Refresh Token 유효 기간 (예: 7일)

    public TokenPair generateAndStoreTokens(Member member) {
        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        // Redis에 Refresh Token 저장 (해시 구조 사용)
        String redisKey = REFRESH_TOKEN_KEY_PREFIX + member.getEmail();
        redisRepository.setValueHashes(redisKey, "token", refreshToken, REFRESH_TOKEN_TTL);

        return new TokenPair(accessToken, refreshToken);
    }

    // Refresh Token 조회 메서드 추가 (필요 시 사용)
    public String getRefreshToken(String email) {
        String redisKey = REFRESH_TOKEN_KEY_PREFIX + email;
        return redisRepository.getValueHashes(redisKey, "token");
    }

    // Refresh Token 삭제 메서드 추가 (로그아웃 등에 사용 가능)
    public void removeRefreshToken(String email) {
        String redisKey = REFRESH_TOKEN_KEY_PREFIX + email;
        redisRepository.remove(redisKey);
    }
}
