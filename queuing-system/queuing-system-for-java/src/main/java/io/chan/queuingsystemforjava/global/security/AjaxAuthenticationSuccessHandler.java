package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenService jwtTokenService;
    private final ResponseWriter responseWriter;

    public AjaxAuthenticationSuccessHandler(JwtTokenService jwtTokenService, ResponseWriter responseWriter) {
        this.jwtTokenService = jwtTokenService;
        this.responseWriter = responseWriter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        MemberContext memberContext = (MemberContext) authentication.getPrincipal();
        Member member = memberContext.getUser();

        // 토큰 생성 및 Redis에 저장
        TokenPair tokenPair = jwtTokenService.generateAndStoreTokens(member);

        // 응답 구성
        LoginResponse loginResponse = LoginResponse.builder()
                .email(member.getEmail())
                .accessToken(tokenPair.accessToken())
                .refreshToken(tokenPair.refreshToken())
                .build();

        // 응답 작성
        responseWriter.writeSuccessResponse(response, loginResponse);
    }
}
