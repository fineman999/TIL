package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private static final String ROLE = "role";

    private final String issuer;
    private final int accessExpirySeconds;  // Access Token 유효 기간
    private final int refreshExpirySeconds; // Refresh Token 유효 기간
    private final SecretKey secretKey;
    private final JwtParser accessTokenParser;
    private final JwtParser refreshTokenParser;

    public JwtProviderImpl(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.access-expiry-seconds}") int accessExpirySeconds,
            @Value("${jwt.refresh-expiry-seconds}") int refreshExpirySeconds,
            @Value("${jwt.secret-key}") String secretKey) {
        this.issuer = issuer;
        this.accessExpirySeconds = accessExpirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenParser = Jwts.parser().verifyWith(this.secretKey).build();
        this.refreshTokenParser = Jwts.parser().verifyWith(this.secretKey).build();
    }

    // Access Token 생성
    @Override
    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessExpirySeconds * 1000L);
        return Jwts.builder()
                .issuer(issuer)
                .issuedAt(now)
                .claim("memberId", member.getMemberId())
                .subject(member.getEmail())
                .expiration(expiresAt)
                .claim(ROLE, member.getMemberRole().getValue())
                .signWith(secretKey)
                .compact();
    }

    // Refresh Token 생성
    @Override
    public String createRefreshToken(Member member) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + refreshExpirySeconds * 1000L);
        return Jwts.builder()
                .issuer(issuer)
                .issuedAt(now)
                .subject(member.getEmail()) // Refresh Token은 최소한의 정보만 포함
                .expiration(expiresAt)
                .signWith(secretKey)
                .compact();
    }

    // Access Token 파싱
    @Override
    public CustomClaims parseAccessToken(String accessToken) {
        try {
            Claims payload = accessTokenParser.parseSignedClaims(accessToken).getPayload();
            String email = payload.getSubject();
            MemberRole memberRole = MemberRole.find(payload.get(ROLE, String.class));
            Long memberId = payload.get("memberId", Long.class);
            return new CustomClaims(memberId, email, memberRole);
        } catch (ExpiredJwtException e) {
            throw new TicketingException(ErrorCode.EXPIRED_TOKEN);
        } catch (RuntimeException e) {
            log.debug("액세스 토큰이 유효하지 않습니다. token={}", accessToken);
            throw new TicketingException(ErrorCode.INVALID_TOKEN);
        }
    }

    // Refresh Token 파싱
    @Override
    public String parseRefreshToken(String refreshToken) {
        try {
            Claims payload = refreshTokenParser.parseSignedClaims(refreshToken).getPayload();
            return payload.getSubject(); // email 반환
        } catch (ExpiredJwtException e) {
            throw new TicketingException(ErrorCode.EXPIRED_TOKEN);
        } catch (RuntimeException e) {
            log.debug("리프레시 토큰이 유효하지 않습니다. token={}", refreshToken);
            throw new TicketingException(ErrorCode.INVALID_TOKEN);
        }
    }
}
