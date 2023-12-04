package io.chan.springsecurityoauth2social.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class OAuth2ProviderUser implements ProviderUser {

    private static final String EMAIL = "email";
    private final OAuth2User oAuth2User;
    private final ClientRegistration clientRegistration;
    private final Map<String, Object> attributes;

    public OAuth2ProviderUser(
            Map<String, Object> authorities,
            OAuth2User oAuth2User,
            ClientRegistration clientRegistration) {
        this.attributes = authorities;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return oAuth2User;
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId(); // properties에 있는 registration 이름
    }

    @Override
    public String getEmail() {
        return Objects.requireNonNull(oAuth2User.getAttribute(EMAIL)).toString();
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


}
