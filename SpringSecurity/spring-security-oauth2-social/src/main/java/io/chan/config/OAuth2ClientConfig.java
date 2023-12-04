package io.chan.config;

import io.chan.springsecurityoauth2social.service.CustomOAuth2UserService;
import io.chan.springsecurityoauth2social.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/images/**", "/js/**", "/css/**", "/lib/**", "/favicon.ico").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/user").hasAnyAuthority("SCOPE_profile", "SCOPE_email")
                .requestMatchers("/api/oidc").hasAnyAuthority("SCOPE_openid")
                .anyRequest().authenticated()
        );

        http.formLogin(formLogin -> formLogin
            .loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/")
            .permitAll()
        );

        http.oauth2Login(
            oauth2Login -> oauth2Login
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                    .oidcUserService(customOidcUserService)
                    .userService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/")
        );

        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        );

        http.logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return new CustomGrantedAuthorityMapper();
    }
}
