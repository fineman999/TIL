package io.chan.springsecurityoauth2social.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
public class User {

    private final String id;
    private final String registrationId;

    private final String username;
    private final String password;

    private final String provider;
    private final String email;
    private final List<? extends GrantedAuthority> authorities;

    @Builder
    public User(String id, String registrationId, String username, String password, String provider, String email, List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.registrationId = registrationId;
        this.username = username;
        this.password = password;
        this.provider = provider;
        this.email = email;
        this.authorities = authorities;
    }


    public static User from(ProviderUser providerUser) {
        return User.builder()
                .id(providerUser.getId())
                .registrationId(providerUser.getProvider())
                .username(providerUser.getUsername())
                .password(providerUser.getPassword())
                .provider(providerUser.getProvider())
                .email(providerUser.getEmail())
                .authorities(providerUser.getAuthorities())
                .build();
    }
}
