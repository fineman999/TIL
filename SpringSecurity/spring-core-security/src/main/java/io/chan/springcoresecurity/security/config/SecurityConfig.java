package io.chan.springcoresecurity.security.config;

import io.chan.springcoresecurity.security.common.FormAuthenticationDetailsSource;
import io.chan.springcoresecurity.security.handler.CustomAccessDeniedHandler;
import io.chan.springcoresecurity.security.handler.CustomAuthenticationSuccessHandler;
import io.chan.springcoresecurity.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    // please use permitAll via HttpSecurity#authorizeHttpRequests instead.
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
//    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 메모리 방식으로 유저 정보를 등록할 수 있다.
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(
//                User.withUsername("user")
//                    .password(passwordEncoder.encode("1111"))
//                    .roles("USER").build());
//        manager.createUser(
//                User.withUsername("manager")
//                    .password(passwordEncoder.encode("1111"))
//                    .roles("MANAGER").build());
//        manager.createUser(
//                User.withUsername("admin")
//                    .password(passwordEncoder.encode("1111"))
//                    .roles("ADMIN").build());
//        return manager;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
            auth -> auth
//                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll()
                .requestMatchers("/","/users","user/login/**","/login*").permitAll()
                .requestMatchers("/mypage").hasAuthority("USER")
                .requestMatchers("/messages").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers("/config").hasAnyAuthority("ADMIN", "MANAGER", "USER")
                .anyRequest().authenticated()
        ).formLogin(
            httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(new FormAuthenticationDetailsSource())
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
        );

        http.exceptionHandling(
            exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .accessDeniedHandler(accessDeniedHandler())
        );
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler("/denied");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
