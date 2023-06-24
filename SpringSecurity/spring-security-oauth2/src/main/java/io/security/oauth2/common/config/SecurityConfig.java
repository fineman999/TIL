package io.security.oauth2.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;


@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http.formLogin(withDefaults());
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .anyRequest().authenticated()
//                );
//        http.httpBasic(withDefaults());
////        http.apply(new CustomSecurityConfigurer().setFlag(false));
//        return http.build();
//    }
}
