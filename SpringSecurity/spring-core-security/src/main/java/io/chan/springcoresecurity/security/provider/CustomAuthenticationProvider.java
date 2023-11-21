package io.chan.springcoresecurity.security.provider;

import io.chan.springcoresecurity.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 사용자가 입력한 정보
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // DB에서 가져온 정보
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return getUsernamePasswordAuthenticationToken(accountContext);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(AccountContext accountContext) {
        return new UsernamePasswordAuthenticationToken(
                accountContext.getAccount(),
                null,
                accountContext.getAuthorities()
        );
    }
}
