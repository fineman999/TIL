package io.chan.springcoresecurity.security.config;

import io.chan.springcoresecurity.security.common.FormAuthenticationDetailsSource;
import io.chan.springcoresecurity.security.factory.UrlResourcesMapFactoryBean;
import io.chan.springcoresecurity.security.filter.PermitAllFilter;
import io.chan.springcoresecurity.security.handler.CustomAuthenticationFailureHandler;
import io.chan.springcoresecurity.security.handler.CustomAuthenticationSuccessHandler;
import io.chan.springcoresecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.chan.springcoresecurity.security.provider.CustomAuthenticationProvider;
import io.chan.springcoresecurity.security.service.SecurityResourceService;
import io.chan.springcoresecurity.security.voter.IpAddressVoter;
import io.chan.springcoresecurity.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

//@Order(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final SecurityResourceService securityResourceService;
    private final RoleHierarchyService roleHierarchyService;
    private final String[] permitAllResources = {"css/**", "/images/**", "/js/**", "/lib/**", "/favicon.ico"};

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
//                .securityMatcher("/**")
                .authorizeHttpRequests(
            auth -> auth
//                .requestMatchers("/images/**", "/js/**", "/css/**", "/lib/**", "/favicon.ico").permitAll()
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
    public PermitAllFilter filterSecurityInterceptor(
     ) throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManager());
        return permitAllFilter;
    }

    private AccessDecisionManager affirmativeBased() {
        return new UnanimousBased(getAccessDecisionVoters());
    }

    /**
     * AccessDecisionVoter를 설정한다.
     * 두 가지 Voter를 설정한다.
     * 1. IpAddressVoter
     * 2. RoleVoter
     */
    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new IpAddressVoter(securityResourceService));
        accessDecisionVoters.add(roleVoter());
        return accessDecisionVoters;
    }
//    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
//        RoleVoter roleVoter = new RoleVoter();
//        roleVoter.setRolePrefix("");
//        return List.of(roleVoter);
//    }


    private AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(roleHierarchyService.findAllHierarchy());
        return roleHierarchy;
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }
}
