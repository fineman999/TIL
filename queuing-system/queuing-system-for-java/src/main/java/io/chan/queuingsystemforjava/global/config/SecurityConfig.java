package io.chan.queuingsystemforjava.global.config;

import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  public static final String API_ENDPOINT = "/api/**";
  private static final String[] API_VERIFICATION_CODE_ENDPOINT = {"/api/v1/auth/**"};
  public static final String LOGIN = "/api/v1/login";
  private final CorsConfigurationSource corsConfigurationSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain apiFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AjaxLoginProcessingFilter ajaxLoginProcessingFilter)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .securityMatcher(API_ENDPOINT)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers(API_VERIFICATION_CODE_ENDPOINT)
                    .hasAnyAuthority("ADMIN", "MANAGER")
                    .requestMatchers("/api/v1/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(ajaxLoginProcessingFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    http.cors(cors -> cors.configurationSource(corsConfigurationSource));

    return http.build();
  }

  @Bean
  public AuthenticationManager ajaxAuthenticationManager(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    AjaxAuthenticationProvider authenticationProvider =
        new AjaxAuthenticationProvider(userDetailsService, passwordEncoder);
    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public AjaxLoginProcessingFilter ajaxLoginProcessingFilter(
      AuthenticationManager authenticationManager,
      JwtTokenService jwtTokenService,
      ResponseWriter responseWriter) {
    AjaxLoginProcessingFilter ajaxLoginProcessingFilter =
        new AjaxLoginProcessingFilter(LOGIN, authenticationManager);
    ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(
        new AjaxAuthenticationSuccessHandler(jwtTokenService, responseWriter));
    return ajaxLoginProcessingFilter;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
          JwtProvider jwtProvider,
          UserDetailsService userDetailsService) {
    return new JwtAuthenticationFilter(jwtProvider, userDetailsService);
  }
}
