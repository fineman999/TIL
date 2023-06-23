package io.security.basicsecurity.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .anyRequest().authenticated()
                );

        http.formLogin(form ->
                        form
//                        .loginPage("/loginPage")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("passwd")
                        .loginProcessingUrl("/login_proc")
                        .successHandler((request, response, authentication) -> {
                            System.out.println("authentication" + authentication.getName());
                            response.sendRedirect("/");
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception" + exception.getMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll());
        http.logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .deleteCookies("remember-me")
                                .addLogoutHandler((request, response, authentication) -> {
                                    request.getSession().invalidate(); // 세션 무효화 - 안해도 LogoutFilter에서 해줌
                                    System.out.println("authentication" + authentication.getName());
                                })
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.sendRedirect("/login"); // 로그아웃 성공시 이동할 페이지
                                })
                                .deleteCookies("remember-me") // 로그아웃시 쿠키 삭제
                );

        http.rememberMe(rememberMe ->
                        rememberMe
                            .rememberMeParameter("remember") // 기본 파라미터명은 remember-me
                            .tokenValiditySeconds(3600) // Default 14일 - 지금은 한시간
                            .alwaysRemember(true) // 리멤버 미 기능이 활성화되지 않아도 항상 실행
                            .userDetailsService(userDetailsService) // 리멤버 미 기능 동작 시 필요
                );

        return http.build();
    }
}
