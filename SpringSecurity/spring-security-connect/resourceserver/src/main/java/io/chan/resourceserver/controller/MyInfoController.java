package io.chan.resourceserver.controller;


import io.chan.sharedobject.Friend;
import io.chan.sharedobject.MyInfo;
import io.chan.sharedobject.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyInfoController {
    private final RestClient restClient;

    @GetMapping("/myInfo")
    public MyInfo myInfo(JwtAuthenticationToken authenticationToken){

        String url = "http://localhost:8083/friends";
        ResponseEntity<List<Friend>> response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders
                        .add(HttpHeaders.AUTHORIZATION, "Bearer " + authenticationToken.getToken().getTokenValue()))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        Photo photo1 = Photo.of("1 ", "Album1 title ", "Album is nice ", "user1");
        Photo photo2 = Photo.of("2 ", "Album2 title ", "Album is beautiful ", "user2");

        return MyInfo.builder().photos(Arrays.asList(photo1, photo2)).friends(response.getBody()).build();
    }
}
