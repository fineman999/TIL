package io.chan.springsecuritydocsexample;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AccessDecisionManagerAuthorizationManagerAdapter<T> implements AuthorizationManager<T> {
    @Override
    public void verify(Supplier<Authentication> authentication, T object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
        return null;
    }
}