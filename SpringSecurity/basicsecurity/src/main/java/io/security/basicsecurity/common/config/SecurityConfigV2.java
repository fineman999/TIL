package io.security.basicsecurity.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfigV2 {

    @Bean
    public SecurityFilterChain section1(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http.formLogin(
            formLogin -> formLogin
                .loginPage("/loginPage")
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

        return http.build();
    }
}
