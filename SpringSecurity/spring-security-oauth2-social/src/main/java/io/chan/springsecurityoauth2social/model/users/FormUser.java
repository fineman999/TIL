package io.chan.springsecurityoauth2social.model.users;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Getter
public class FormUser implements ProviderUser {

    private final String id;
    private final String username;
    private final String password;
    private final String email;
    private final String provider;
    private final List<? extends GrantedAuthority> authorities;
    private boolean isCertificated;

    @Builder
    public FormUser(String id, String username, String password, String email, String provider, List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.authorities = authorities;
        isCertificated = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPicture() {
        return null;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return null;
    }

    @Override
    public void isCertificated(boolean bool) {
        isCertificated = bool;
    }


}
