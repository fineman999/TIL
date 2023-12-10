package io.chan.springsecurityauthorization.configs;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        authorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint ->
                authorizationEndpoint
                        .authenticationProvider(customAuthenticationProvider)
                        .authorizationResponseHandler(((request, response, authentication) -> {
                            OAuth2AuthorizationCodeRequestAuthenticationToken authentication1 = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
                            System.out.println(authentication);
                            String redirectUri = authentication1.getRedirectUri();
                            String authorizationCode = Objects.requireNonNull(authentication1.getAuthorizationCode()).getTokenValue();
                            String state = null;
                            if (StringUtils.hasText(authentication1.getState())) {
                                state = authentication1.getState();
                            }
                            response.sendRedirect(redirectUri+"?code="+authorizationCode+"&state="+state);
                        }))
                        .errorResponseHandler((request, response, exception) -> {
                            System.out.println(exception.toString());
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        })
        );

        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers(endpointsMatcher).authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .with(authorizationServerConfigurer, Customizer.withDefaults());
        http
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                );
        return http.build();
    }

}
