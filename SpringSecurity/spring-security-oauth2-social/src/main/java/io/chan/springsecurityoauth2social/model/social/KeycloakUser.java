package io.chan.springsecurityoauth2social.model.social;

import io.chan.springsecurityoauth2social.model.Attributes;
import io.chan.springsecurityoauth2social.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KeycloakUser extends OAuth2ProviderUser {

    private static final String ID = "sub";
    private static final String USERNAME = "preferred_username";

    public KeycloakUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return getAttributes().get(ID).toString();
    }

    @Override
    public String getUsername() {
        return getAttributes().get(USERNAME).toString();
    }

    @Override
    public String getPicture() {
        return null;
    }
}
