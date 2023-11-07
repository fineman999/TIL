package io.security.basicsecurity.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigV2 {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain section1(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http.formLogin(
            formLogin -> formLogin
//                .loginPage("/loginPage")
                .defaultSuccessUrl("/user", true)
                .failureUrl("/loginPage?error")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .loginProcessingUrl("/login_proc")
                .successHandler((request, response, authentication) -> {
                    log.info("authentication = {}", authentication);
                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {
                    log.info("exception = {}", exception.getMessage());
                    response.sendRedirect("/loginPage");
                })
                .permitAll()
        );

        http.logout(
            logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("remember-me")
                .addLogoutHandler((request, response, authentication) -> {
                    request.getSession().invalidate();
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/login");
                })
        );

        http.rememberMe(
            rememberMe -> rememberMe
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .alwaysRemember(false)
                .userDetailsService(userDetailsService)
        );

        http.sessionManagement(
            sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
        );
        return http.build();
    }
}
