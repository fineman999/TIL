package io.chan.springsecuritydocsexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    DefaultSecurityFilterChain springSecurity(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");
        http
                // ...
                .requestCache((cache) -> cache
                        .requestCache(requestCache)
                );
        return http.build();
    }
}
