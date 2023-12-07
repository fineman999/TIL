package io.chan.springsecurityresources.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public String index(
    ) {return "index";
    }

    @GetMapping("/api/user")
    public Authentication user(
            Authentication authentication
    ) {
        log.info("authentication: {}", authentication);
        return authentication;
    }
}
