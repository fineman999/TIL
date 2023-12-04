package io.chan.springsecurityoauth2social.controller;

import io.chan.springsecurityoauth2social.model.PrincipalUser;
import io.chan.springsecurityoauth2social.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal PrincipalUser principalUser
            ) {

        if (authentication == null) {
            return "index";
        }
        String name = userService.getName(principalUser);
        model.addAttribute("user", name);
        model.addAttribute("provider", principalUser.providerUser().getProvider());
        return "index";
    }
}
