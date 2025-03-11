package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWT 인증 필터 실행");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.nonNull(request.getHeader(authHeader))) {
            String accessToken = removeBearer(authHeader);
            // DB에서 가져온 정보
            MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(accessToken);
            insertAuthentication(request, memberContext);
            log.debug("JWT 인증 필터 성공");
        }
        log.debug("JWT 인증 필터 종료");
        filterChain.doFilter(request, response);
    }

    private String removeBearer(String bearerAccessToken) {
        if (bearerAccessToken.contains(BEARER)) {
            return bearerAccessToken.replace(BEARER, "");
        }
        throw new TicketingException(ErrorCode.INVALID_TOKEN_HEADER);
    }

    private void insertAuthentication(HttpServletRequest request, MemberContext userDetails) {
        AuthenticationToken token = getAuthenticationToken(request, userDetails);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private AuthenticationToken getAuthenticationToken(HttpServletRequest request, MemberContext memberContext) {
        AuthenticationToken authenticationToken = new AuthenticationToken(
                memberContext,
                null,
                memberContext.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }
}
