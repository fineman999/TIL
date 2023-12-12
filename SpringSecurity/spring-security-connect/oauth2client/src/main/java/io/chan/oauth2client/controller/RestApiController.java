package io.chan.oauth2client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final RestClient restClient;

    @GetMapping("/token")
    public OAuth2AccessToken getAccessToken(
            @RegisteredOAuth2AuthorizedClient("keycloak")
            OAuth2AuthorizedClient authorizedClient
    ) {
        return authorizedClient.getAccessToken();
    }

    @GetMapping("/photos")
    public List<Photo> getPhotos(AccessToken accessToken) {
        String url = "http://localhost:8082/photos";

        ResponseEntity<List<Photo>> response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders
                        .add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.accessToken()))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        assert response != null;

        return response.getBody();
    }
}
