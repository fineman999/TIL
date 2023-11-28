package io.security.oauth2.common.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

import static org.springframework.security.config.Customizer.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
//      form login is enabled by default
        http.formLogin(withDefaults());

        // http basic is enabled by default
        http.httpBasic(withDefaults());

        // customAuthenticationEntryPoint is enabled by default
        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new AuthenticationEntryPoint
                () {
                      @Override
                      public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                          System.out.printf("AuthenticationEntryPoint commence");
                      }
                  }
            )
        );
        return http.build();
    }
    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http.httpBasic(withDefaults());
//        http.apply(new CustomSecurityConfigurer().setFlag(false));
        return http.build();
    }
}
