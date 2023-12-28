package io.chan.springsecuritydocsexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/endpoint")
    public String endpoint() {
        return "endpoint";
    }

    @GetMapping("/test")
    public String any() {
        return "any";
    }
}
