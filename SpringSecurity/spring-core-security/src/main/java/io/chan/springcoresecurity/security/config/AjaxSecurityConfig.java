package io.chan.springcoresecurity.security.config;

import io.chan.springcoresecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import io.chan.springcoresecurity.security.filter.AjaxLoginProcessingFilter;
import io.chan.springcoresecurity.security.handler.AjaxAccessDeniedHandler;
import io.chan.springcoresecurity.security.handler.AjaxAuthenticationFailureHandler;
import io.chan.springcoresecurity.security.handler.AjaxAuthenticationSuccessHandler;
import io.chan.springcoresecurity.security.provider.AjaxAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Order(0)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AjaxSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider(
    ) {
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }


    @Bean
    public SecurityFilterChain ajaxSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
            auth -> auth
                .requestMatchers("/api/messages").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
        );


        http.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(
            exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                .accessDeniedHandler(new AjaxAccessDeniedHandler())
        );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {

        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(ajaxAuthenticationManager());
        ajaxLoginProcessingFilter.setSecurityContextRepository(securityContextRepository());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new HttpSessionSecurityContextRepository(),
                new RequestAttributeSecurityContextRepository()
        );
    }

    @Bean
    public AuthenticationManager ajaxAuthenticationManager() throws Exception {
        ProviderManager authenticationManager;
        try {
            authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException("AuthenticationManager could not be obtained.");
        }
        authenticationManager.getProviders().add(ajaxAuthenticationProvider());
        return authenticationConfiguration.getAuthenticationManager();
    }


}
