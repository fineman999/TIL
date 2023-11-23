package io.chan.springcoresecurity.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {

    @GetMapping("/api/messages")
    public String apiMessage() {
        return "messages";
    }
}
