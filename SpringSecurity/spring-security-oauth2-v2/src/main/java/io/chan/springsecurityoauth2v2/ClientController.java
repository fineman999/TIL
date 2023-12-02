package io.chan.springsecurityoauth2v2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ClientController {


    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @GetMapping("/client")
    public ClientDto client(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientRegistrationId = "keycloak";
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientRepository.loadAuthorizedClient(
                clientRegistrationId,
                authentication,
                request
        );

        OAuth2AuthorizedClient oAuth2AuthorizedClient2 = oAuth2AuthorizedClientService.loadAuthorizedClient(
                clientRegistrationId,
                authentication.getName()
        );

        OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();


        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(oAuth2AuthorizedClient.getClientRegistration(), accessToken));

        // 인증 결과
        OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(
                oAuth2User,
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                clientRegistrationId
        );

        // 인증 결과를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);

        return ClientDto.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(Objects.requireNonNull(oAuth2AuthorizedClient.getRefreshToken()).getTokenValue())
                .clientName(oAuth2AuthorizedClient.getClientRegistration().getClientName())
                .principalName(oAuth2AuthorizedClient.getPrincipalName())
                .build();
    }
}
