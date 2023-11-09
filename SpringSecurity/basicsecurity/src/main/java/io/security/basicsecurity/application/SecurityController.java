package io.security.basicsecurity.application;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class SecurityController {

    @GetMapping("/")
    public String index(
            HttpSession session,
            Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication = {}", authentication);
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        log.info("context = {}", context);
        Authentication authentication1 = context.getAuthentication();
        log.info("authentication1 = {}", authentication1);
        log.info("principal = {}", principal);
        return "home";
    }

    @GetMapping("/thread")
    public String thread() {
        new Thread(
                () -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                }
        ).start();
        return "thread";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }

    @GetMapping("/admin/**")
    public String adminAll() {
        return "adminAll";
    }

    @GetMapping("/shop/**")
    public String shop() {
        return "shop";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }
}
