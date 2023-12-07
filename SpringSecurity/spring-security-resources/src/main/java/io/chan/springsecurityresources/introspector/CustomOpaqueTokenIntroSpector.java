package io.chan.springsecurityresources.introspector;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomOpaqueTokenIntroSpector implements OpaqueTokenIntrospector {

    private final OAuth2ResourceServerProperties properties;
    private final OpaqueTokenIntrospector delegate;

    public CustomOpaqueTokenIntroSpector(OAuth2ResourceServerProperties properties) {
        this.properties = properties;
        this.delegate = new NimbusOpaqueTokenIntrospector(
                properties.getOpaquetoken().getIntrospectionUri(),
                properties.getOpaquetoken().getClientId(),
                properties.getOpaquetoken().getClientSecret()
        );
    }


    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {

        OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);

        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(),
                principal.getAttributes(),
                extractAuthorities(principal)
        );
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        List<String> scope = principal.getAttribute(OAuth2TokenIntrospectionClaimNames.SCOPE);
        assert scope != null;
        return scope.stream().map(authority ->
                "SCOPE_" + authority.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
