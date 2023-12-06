package io.chan.springsecurityresources.filter.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.springsecurityresources.dto.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDto loginDto;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        } catch (IOException e) {
            log.error("Binding Exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

        // UsernamePasswordAuthenticationToken을 AuthenticationManager에게 전달하여 인증을 요청한다.
        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    // 인증이 성공하면 successfulAuthentication 메서드가 호출된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // 인증이 성공하면 SecurityContext에 인증 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authResult);

        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
