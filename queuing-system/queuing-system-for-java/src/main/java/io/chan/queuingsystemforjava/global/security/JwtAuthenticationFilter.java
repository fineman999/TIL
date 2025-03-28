package io.chan.queuingsystemforjava.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.ErrorResponse;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String BEARER = "Bearer ";

  private final JwtProvider jwtProvider;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.debug("JWT 인증 필터 실행");
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (Objects.nonNull(authHeader)) {
      try {
        String accessToken = removeBearer(authHeader);
        log.debug("Extracted access token: {}", accessToken);
        CustomClaims claims = jwtProvider.parseAccessToken(accessToken);
        MemberContext memberContext =
                (MemberContext) userDetailsService.loadUserByUsername(claims.email());
        insertAuthentication(request, memberContext);
        log.debug("JWT 인증 성공: email={}", claims.email());
      } catch (TicketingException e) {
        log.warn("JWT 인증 실패: code={}, message={}", e.getErrorCode(), e.getMessage());
        response.setStatus(e.getErrorCode().getHttpStatusValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getErrorCode()));
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private String removeBearer(String bearerToken) {
    if (bearerToken.startsWith(BEARER)) {
      return bearerToken.substring(BEARER.length());
    }
    throw new TicketingException(ErrorCode.INVALID_TOKEN_HEADER);
  }

  private void insertAuthentication(HttpServletRequest request, MemberContext userDetails) {
    AuthenticationToken token = getAuthenticationToken(request, userDetails);
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  private AuthenticationToken getAuthenticationToken(
      HttpServletRequest request, MemberContext memberContext) {
    AuthenticationToken authenticationToken =
        new AuthenticationToken(memberContext, null, memberContext.getAuthorities());
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authenticationToken;
  }
}
