package io.security.basicsecurity.application;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index(HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication1 = context.getAuthentication();
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


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }
}
