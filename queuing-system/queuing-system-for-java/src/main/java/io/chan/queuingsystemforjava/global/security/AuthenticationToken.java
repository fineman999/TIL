package io.chan.queuingsystemforjava.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

public class AuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    private Object credentials;

    /**
     * 인증을 받기 전에 사용자가 입력하는 로그인 정보를 담는 생성자
     */
    public AuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * 인증을 받은 후에 그 결과의 사용자 정보를 담는 생성자
     */
    public AuthenticationToken(Object principal, Object credentials,
                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * unauthenticated <code>AjaxAuthenticationToken</code>.
     *
     * @param principal
     * @param credentials
     * @return AjaxAuthenticationToken with false isAuthenticated() result
     * @since 5.7
     */
    public static AuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new AuthenticationToken(principal, credentials);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * authenticated <code>AjaxAuthenticationToken</code>.
     *
     * @param principal
     * @param credentials
     * @return AjaxAuthenticationToken with true isAuthenticated() result
     * @since 5.7
     */
    public static AuthenticationToken authenticated(Object principal, Object credentials,
                                                        Collection<? extends GrantedAuthority> authorities) {
        return new AuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
