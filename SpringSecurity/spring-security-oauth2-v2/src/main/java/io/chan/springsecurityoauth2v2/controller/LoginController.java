package io.chan.springsecurityoauth2v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/oauth2Login")
    public String loginPage(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutPage(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
