package io.chan.oauth2client.controller;

import io.chan.sharedobject.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final RestClient restClient;
    private final DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/token")
    public OAuth2AccessToken getAccessToken(
            @RegisteredOAuth2AuthorizedClient("keycloak")
            OAuth2AuthorizedClient authorizedClient
    ) {
        return authorizedClient.getAccessToken();
    }

    @GetMapping("/tokenExpire")
    public Map<String,Object> tokenExpire(AccessToken accessToken){

        String url = "http://localhost:8082/tokenExpire";
        ResponseEntity<Map<String, Object>> response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders
                        .add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken()))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assert response != null;
        return response.getBody();
    }


    @GetMapping("/newAccessToken")
    public OAuth2AccessToken newAccessToken(
            OAuth2AuthenticationToken authentication,
            HttpServletRequest request, HttpServletResponse response){

        OAuth2AuthorizedClient authorizedClient
                = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {

            ClientRegistration clientRegistration = ClientRegistration.withClientRegistration
                            (authorizedClient.getClientRegistration()).authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .build();

            OAuth2AuthorizedClient oAuth2AuthorizedClient =
                    new OAuth2AuthorizedClient(clientRegistration, authorizedClient.getPrincipalName(),
                            authorizedClient.getAccessToken(),authorizedClient.getRefreshToken());

            OAuth2AuthorizeRequest oAuth2AuthorizeRequest =
                    OAuth2AuthorizeRequest.withAuthorizedClient(oAuth2AuthorizedClient)
                            .principal(authentication)
                            .attribute(HttpServletRequest.class.getName(), request)
                            .attribute(HttpServletResponse.class.getName(), response)
                            .build();

            authorizedClient = oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);
        }

        return authorizedClient.getAccessToken();
    }
}
