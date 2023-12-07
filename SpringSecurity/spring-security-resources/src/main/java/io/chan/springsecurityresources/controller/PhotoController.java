package io.chan.springsecurityresources.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    @GetMapping("/photos/1")
    public Photo getPhoto() {
        return Photo.builder()
                .userId("user1")
                .photoId("1")
                .photoTitle("Photo 1")
                .photoDescription("Description of Photo 1")
                .build();
    }
}
