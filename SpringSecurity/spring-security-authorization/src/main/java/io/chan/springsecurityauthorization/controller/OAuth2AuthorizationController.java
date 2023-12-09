package io.chan.springsecurityauthorization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2AuthorizationController {

    private final OAuth2AuthorizationService authorizationService;

    @GetMapping("/oauth2/authorizations")
    public OAuth2Authorization getAuthorizations(String token) {
        return authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
    }
}
