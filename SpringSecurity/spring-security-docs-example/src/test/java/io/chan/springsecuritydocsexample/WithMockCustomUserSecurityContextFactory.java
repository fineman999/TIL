package io.chan.springsecuritydocsexample;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Collection<GrantedAuthority> authorities = Arrays.asList(() -> "ROLE_USER", () -> "ROLE_ADMIN");
        User user = new User(customUser.username(), customUser.password(), authorities);
        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(user, customUser.password(), authorities);
        context.setAuthentication(auth);
        return context;
    }

}
