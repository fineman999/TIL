package io.security.basicsecurity.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;


@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static UserDetailsManager users() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}1111")
                .roles("USER","SYS")
                .build();

        UserDetails sys = User.builder()
                .username("sys")
                .password("{noop}1111")
                .roles("SYS")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}1111")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, sys, admin);
    }

//    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
         http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http
            .formLogin(Customizer.withDefaults());

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        return http.build();
    }

//    @Bean
//    @Order(0)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/user").hasRole("USER")
                                .requestMatchers("/admin/pay").hasRole("ADMIN")
                                .requestMatchers("/admin/**")
                                .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('SYS')"))
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
                            HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                            SavedRequest savedRequest = requestCache.getRequest(request, response);
                            String redirectUrl = savedRequest.getRedirectUrl();
                            response.sendRedirect(redirectUrl);
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println("exception" + exception.getMessage());
//                            response.sendRedirect("/login");
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

        http.sessionManagement(sessionManagement ->
                        sessionManagement
                                .maximumSessions(1) // 최대 허용 가능 세션 수, -1 무제한 로그인 세션 허용
                                .maxSessionsPreventsLogin(false) // 동시 로그인 차단, 기존 사용자 세션 만료(default: false) or 신규 로그인 차단
                                .expiredUrl("/expired") // 세션이 만료된 경우 이동할 페이지
                );
        http.sessionManagement(session ->
                        session.sessionFixation(
                                SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId // 세션 고정 보호 - 기본값
                        )
                );
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)); // 세션 정책 - 기본값

        http.exceptionHandling(exceptionHandling ->
                        exceptionHandling
//                                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/loginPage")) // 인증 예외 처리자
                                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/denied")) // 인가 예외 처리자
                );

        http.csrf(AbstractHttpConfigurer::disable); // CSRF 토큰 비활성화
        return http.build();
    }
}
