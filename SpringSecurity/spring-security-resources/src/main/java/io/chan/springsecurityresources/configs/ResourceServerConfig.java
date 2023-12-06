package io.chan.springsecurityresources.configs;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import io.chan.springsecurityresources.filter.authentication.JwtAuthenticationFilter;
import io.chan.springsecurityresources.filter.authorization.JwtAuthorizationRsaFilter;
import io.chan.springsecurityresources.signature.RsaSecuritySigner;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class ResourceServerConfig {

    // oauth2ResourceServer와 관련된 설정을 제공하는 클래스
//    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    // AuthenticationManager를 주입받기 위한 설정
    private final AuthenticationConfiguration authenticationConfiguration;

    // 대칭키 암호화를 위한 빈 등록
//    private final MacSecuritySigner macSecuritySigner;
//    private final OctetSequenceKey octetSequenceKey;

    // 비대칭키 암호화를 위한 빈 등록
    private final RSAKey rsaKey;
    private final RsaSecuritySigner rsaSecuritySigner;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf 비활성화
        // csrf 은 세션/쿠기 기반의 보안관련 취약점을 이용하는 기법
        // jwt 를 사용할 경우에는 대부분 세션은 사용하지 않기 때문에 csrf 기능은 비활성화 하는 것이 맞다.
        // 다만  jwt 를 쿠키에 저장해서 보안처리를 할 경우에는 csrf 에 취약한 부분이 발생하기 때문에 고려해야 하는 것은 맞다.
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션 생성 정책 설정
        // 세션을 사용하지 않기 때문에 세션 생성 정책을 STATELESS 로 설정한다.
        http.sessionManagement(sessionManagement ->
            sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        http
            .authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            );

        // 로그인 정보를 얻기 위한 설정
        http.userDetailsService(userDetailsService());

        // jwt 토큰을 검증하기 위한 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        // jwt 토큰을 검증하기 위한 필터 추가 - jwtDecoder를 사용하는 필터
        http.oauth2ResourceServer((oauth2) -> oauth2
                .jwt(Customizer.withDefaults())
        );

        // 비 대칭키를 사용하는 필터 추가
//        http.addFilterBefore(jwtAuthorizationRsaFilter(rsaKey), UsernamePasswordAuthenticationFilter.class);


        // jwt 토큰을 검증하기 위한 필터 추가 - 직접 구현한 필터
//        http.addFilterBefore(jwtAuthenticationMacFilter(octetSequenceKey), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    // Mac 방식으로  검증하기 위한 필터 추가 - 직접 구현한 필터
//    @Bean
//    public JwtAuthorizationMacFilter jwtAuthenticationMacFilter(OctetSequenceKey octetSequenceKey) {
//        return new JwtAuthorizationMacFilter(octetSequenceKey);
//    }

    // Rsa 방식으로 검증하기 위한 필터 추가 - 직접 구현한 필터
    @Bean
    public JwtAuthorizationRsaFilter jwtAuthorizationRsaFilter(RSAKey rsaKey) throws JOSEException {
        return new JwtAuthorizationRsaFilter(new RSASSAVerifier(rsaKey));
    }

    // AuthenticationManager를 주입받기 위한 설정
    // AuthenticationConfiguration에서 제공하는 메서드를 사용하여 AuthenticationManager를 주입받는다.
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                rsaSecuritySigner,
                rsaKey
        );
        //  authenticationManager()는 WebSecurityConfigurerAdapter에서 제공하는 메서드이므로
        // AuthenticationManager를 주입받아야 한다.
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password("{noop}1234")
                .authorities("ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
//    }

//    @Bean
//    public JwtDecoder jwtDecoderByOidc() {
//        return JwtDecoders.fromOidcIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri());
//    }

//    @Bean
//    public JwtDecoder nimbusJwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(oAuth2ResourceServerProperties.getJwt().getJwkSetUri())
//                .jwsAlgorithm(SignatureAlgorithm.RS512)
//                .build();
//    }
}
