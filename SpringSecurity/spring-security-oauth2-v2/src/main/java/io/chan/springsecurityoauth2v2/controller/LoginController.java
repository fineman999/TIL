package io.chan.springsecurityoauth2v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;

    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    private final Duration clockSkew = Duration.ofSeconds(3600);
    private final Clock clock = Clock.systemUTC();

    @GetMapping("/oauth2Login")
    public String loginPage(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> httpServlet = new HashMap<>();
        httpServlet.put(HttpServletRequest.class.getName(), request);
        httpServlet.put(HttpServletResponse.class.getName(), response);


        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal(authentication)
                .attributes(attributes -> attributes.putAll(httpServlet))
                .build();

        OAuth2AuthorizationSuccessHandler oAuth2AuthorizationSuccessHandler = (OAuth2AuthorizedClient, principal, attributes) -> {
            oAuth2AuthorizedClientRepository.saveAuthorizedClient(
                    OAuth2AuthorizedClient,
                    principal,
                    (HttpServletRequest) attributes.get(HttpServletRequest.class.getName()),
                    (HttpServletResponse) attributes.get(HttpServletResponse.class.getName())
            );
        };


        authorizedClientManager.setAuthorizationSuccessHandler(oAuth2AuthorizationSuccessHandler);

        OAuth2AuthorizedClient authorize = authorizedClientManager.authorize(authorizeRequest);


        if (authorize != null) {

            if (hasTokenExpired(authorize.getAccessToken()) && authorize.getRefreshToken() != null) {
                authorizedClientManager.authorize(authorizeRequest);
            }

            DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
            ClientRegistration clientRegistration = authorize.getClientRegistration();
            OAuth2AccessToken accessToken = authorize.getAccessToken();
            OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, accessToken);
            OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);


            SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
            simpleAuthorityMapper.setPrefix("SYSTEM_");
            Set<GrantedAuthority> grantedAuthorities = simpleAuthorityMapper.mapAuthorities(oAuth2User.getAuthorities());

            OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(
                    oAuth2User,
                    grantedAuthorities,
                    clientRegistration.getRegistrationId()
            );

            SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);

            model.addAttribute("oAuth2AuthenticationToken", oAuth2AuthenticationToken);
        }
            log.info("authorize: {}", authorize);
        return "home";
    }
    @GetMapping("/clientCredentialsLogin")
    public String clientCredentialsLogin(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> httpServlet = new HashMap<>();
        httpServlet.put(HttpServletRequest.class.getName(), request);
        httpServlet.put(HttpServletResponse.class.getName(), response);


        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal(authentication)
                .attributes(attributes -> attributes.putAll(httpServlet))
                .build();

        OAuth2AuthorizationSuccessHandler oAuth2AuthorizationSuccessHandler = (OAuth2AuthorizedClient, principal, attributes) -> {
            oAuth2AuthorizedClientRepository.saveAuthorizedClient(
                    OAuth2AuthorizedClient,
                    principal,
                    (HttpServletRequest) attributes.get(HttpServletRequest.class.getName()),
                    (HttpServletResponse) attributes.get(HttpServletResponse.class.getName())
            );
        };


        authorizedClientManager.setAuthorizationSuccessHandler(oAuth2AuthorizationSuccessHandler);

        OAuth2AuthorizedClient authorize = authorizedClientManager.authorize(authorizeRequest);

        log.info("authorize: {}", authorize.getAccessToken().getTokenType());
        model.addAttribute("authorizedClient", authorize.getAccessToken().getTokenValue());
        return "home";
    }

    private boolean hasTokenExpired(OAuth2AccessToken accessToken) {
        return this.clock.instant().isAfter(Objects.requireNonNull(accessToken.getExpiresAt()).minus(this.clockSkew));
    }

    @GetMapping("/logout")
    public String logoutPage(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
