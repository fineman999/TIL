package io.chan.springsecurityoauth2social.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/user")
    public OAuth2User user(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) {

        log.info("authentication: {}", authentication);
        log.info("oAuth2User: {}", oAuth2User);
        return oAuth2User;
    }

    @GetMapping("/oidc")
    public OidcUser oidcUser(
            Authentication authentication,
            @AuthenticationPrincipal OidcUser oidcUser
    ) {

        log.info("authentication: {}", authentication);
        log.info("oidcUser: {}", oidcUser);
        return oidcUser;
    }

}
