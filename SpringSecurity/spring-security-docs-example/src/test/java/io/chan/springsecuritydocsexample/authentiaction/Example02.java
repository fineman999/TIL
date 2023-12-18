package io.chan.springsecuritydocsexample.authentiaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class Example02 {

    @Test
    @DisplayName("SecurityContextHolder")
    void test() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new TestingAuthenticationToken("username", "password", "ROLE_USER");
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

    }
}
