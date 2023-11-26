package io.chan.springcoresecurity.security.config;

import io.chan.springcoresecurity.security.common.FormAuthenticationDetailsSource;
import io.chan.springcoresecurity.security.factory.UrlResourcesMapFactoryBean;
import io.chan.springcoresecurity.security.handler.CustomAuthenticationFailureHandler;
import io.chan.springcoresecurity.security.handler.CustomAuthenticationSuccessHandler;
import io.chan.springcoresecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.chan.springcoresecurity.security.provider.CustomAuthenticationProvider;
import io.chan.springcoresecurity.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

//@Order(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final SecurityResourceService securityResourceService;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/images/**", "/js/**", "/css/**", "/lib/**", "/favicon.ico").permitAll()
                .requestMatchers("/","/users","user/login/**","/login*").permitAll()
//                .requestMatchers("/mypage").hasRole("USER")
//                .requestMatchers("/messages").hasAnyRole("MANAGER", "ADMIN")
//                .requestMatchers("/config").hasAnyRole("ADMIN", "MANAGER", "USER")
                    .anyRequest().authenticated()
        ).formLogin(
            httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(new FormAuthenticationDetailsSource())
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()
        );

        http.addFilterBefore(filterSecurityInterceptor(), AuthorizationFilter.class);

        http.exceptionHandling(
            exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                    .accessDeniedHandler(accessDeniedHandler())
        );

        return http.build();

    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String deniedUrl = "/denied";
            response.sendRedirect(deniedUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(
     ) throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        return filterSecurityInterceptor;
    }

    private AccessDecisionManager affirmativeBased() {
        return new UnanimousBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        return List.of(roleVoter);
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }
}
