package io.chan.queuingsystemforjava.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.member.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
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

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

    AuthenticationToken ajaxAuthenticationToken =
        new AuthenticationToken(loginRequest.email(), loginRequest.password());
        this.setDetails(request, ajaxAuthenticationToken);
        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }
    protected void setDetails(HttpServletRequest request, AuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
