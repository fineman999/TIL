package io.chan.springsecurityoauth2social.controller;

import io.chan.springsecurityoauth2social.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping("/")
    public String indexPage(
            Model model,
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User providerUser
            ) {


        String name = userService.getName(authentication, providerUser);
        model.addAttribute("name", name);

        return "index";
    }
}
