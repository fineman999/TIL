package io.chan.resourceserver.controller;

import io.chan.sharedobject.Photo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PhotoController {

    @GetMapping("/photos")
    public List<Photo> getPhotos(
            Authentication authentication
    ) {
        log.info("Principal: {}", authentication.getPrincipal());
        Photo photo = Photo.of("1", "Photo 1", "Photo 1 description", "1");
        Photo photo2 = Photo.of("2", "Photo 2", "Photo 2 description", "2");

        return List.of(photo, photo2);
    }

    @GetMapping("/remotePhotos")
    public List<Photo> getRemotePhotos() {
        Photo photo = Photo.of("1", "Photo 1", "Photo 1 description", "1");
        Photo photo2 = Photo.of("2", "Photo 2", "Photo 2 description", "2");

        return List.of(photo, photo2);
    }

}
