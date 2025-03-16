package io.chan.queuingsystemforjava.global.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AjaxAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AjaxAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 사용자가 입력한 정보
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // DB에서 가져온 정보
        MemberContext MemberContext  = (MemberContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, MemberContext.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return getAuthenticationToken(MemberContext);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }

    private AuthenticationToken getAuthenticationToken(MemberContext memberContext) {
        return new AuthenticationToken(
                memberContext,
                null,
                memberContext.getAuthorities()
        );
    }
}
