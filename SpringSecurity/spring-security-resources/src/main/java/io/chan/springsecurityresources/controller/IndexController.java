package io.chan.springsecurityresources.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public OpaqueDto index(
            Authentication authentication
    ) {
        BearerTokenAuthentication tokenAuthentication = (BearerTokenAuthentication) authentication;
        Map<String, Object> tokenAttributes = tokenAuthentication.getTokenAttributes();
        boolean active = (boolean) tokenAttributes.get("active");
        return OpaqueDto.builder()
                .active(active)
                .authentication(authentication)
                .principal(authentication.getPrincipal())
                .build();
    }

    @GetMapping("/api/user")
    public Authentication user(
            Authentication authentication,
            @AuthenticationPrincipal Jwt principal
            ) throws URISyntaxException {
        log.info("authentication: {}", authentication);
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String sub = (String) jwtAuthenticationToken.getTokenAttributes().get("sub");
        String email= (String) jwtAuthenticationToken.getTokenAttributes().get("email");
        String scope = (String) jwtAuthenticationToken.getTokenAttributes().get("scope");

        String sub1 = principal.getClaimAsString("sub");



        // RestTemplate을 사용하여 외부 Server  엔드포인트에 접근한다.
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER_SPACE + principal.getTokenValue());
//
//        RequestEntity<String> request = new RequestEntity<>(httpHeaders, HttpMethod.GET, new URI("http://localhost:8081/api/user"));
//        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
//        String body = response.getBody();


        return authentication;
    }
}
