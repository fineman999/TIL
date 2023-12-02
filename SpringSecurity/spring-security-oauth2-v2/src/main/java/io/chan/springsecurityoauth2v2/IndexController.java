package io.chan.springsecurityoauth2v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ietf.jgss.Oid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IndexController {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String index() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");

        String clientId = clientRegistration.getClientId();
        log.info("clientId: {}", clientId);

        String redirectUri = clientRegistration.getRedirectUri();
        log.info("redirectUri: {}", redirectUri);
        return "index";
    }

    // OAuth2User 방식
    @GetMapping("/user")
    public OAuth2User user(String accessToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessToken,
                Instant.now(),
                Instant.MAX);

        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

        return defaultOAuth2UserService.loadUser(oAuth2UserRequest);
    }

    // OicdUser 방식
     @GetMapping("/oidc")
    public OAuth2User oidc(String accessToken, String idToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
             OAuth2AccessToken.TokenType.BEARER,
             accessToken,
             Instant.now(),
             Instant.MAX);

        // 인가 서버에서 받은 idToken을 파싱하여 claims를 만들어야 한다.
        Map<String, Object> claims = new HashMap<>();
        claims.put(IdTokenClaimNames.ISS, "http://localhost:8080/realms/oauth2");
        claims.put(IdTokenClaimNames.SUB, "OIDC0");
        claims.put("preferred_username", "chan");


        OidcIdToken oidcIdToken = new OidcIdToken(idToken, Instant.now(), Instant.MAX, claims);

        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
        OidcUserService oidcUserService = new OidcUserService();

        return oidcUserService.loadUser(oidcUserRequest);
    }

    @GetMapping("/authentication")
    public OAuth2User authentication(Authentication authentication) {
            OAuth2AuthenticationToken authentication1 = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authentication2 = (OAuth2AuthenticationToken) authentication;
        log.info("authentication1: {}", authentication1);
        log.info("authentication2: {}", authentication2);
        return authentication1.getPrincipal();
    }

    @GetMapping("/oauth2User")
    public OAuth2User oauth2User(@AuthenticationPrincipal OAuth2User authentication) {
        log.info("authentication: {}", authentication);
        return authentication;
    }

    @GetMapping("/oidcUser")
    public OidcUser oidcUser(@AuthenticationPrincipal OidcUser authentication) {
        log.info("authentication: {}", authentication);
        return authentication;
    }
}
