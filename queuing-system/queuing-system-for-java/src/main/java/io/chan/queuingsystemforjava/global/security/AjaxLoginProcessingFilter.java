package io.chan.queuingsystemforjava.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.ErrorResponse;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter(String ajaxLoginProcessingUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(ajaxLoginProcessingUrl, HttpMethod.POST.name()), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if(!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new IllegalStateException("Authentication is not supported");
        }

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            AuthenticationToken ajaxAuthenticationToken =
                    new AuthenticationToken(loginRequest.email(), loginRequest.password());
            this.setDetails(request, ajaxAuthenticationToken);
            return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
        } catch (ValueInstantiationException e) {
            // email 또는 password가 유효하지 않을 때
            ErrorCode invalidInputValue = ErrorCode.INVALID_INPUT_VALUE;
            response.setStatus(invalidInputValue.getHttpStatusValue());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), ErrorResponse.of(invalidInputValue));
            return null; // 인증 중단
        } catch (IOException e) {
            // JSON 파싱 실패 등 기타 IO 문제
            ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
            response.setStatus(internalServerError.getHttpStatusValue());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), ErrorResponse.of(internalServerError));
            return null;
        }
    }
    protected void setDetails(HttpServletRequest request, AuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
