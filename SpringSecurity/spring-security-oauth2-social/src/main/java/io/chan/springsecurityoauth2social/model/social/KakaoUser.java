package io.chan.springsecurityoauth2social.model.social;

import io.chan.springsecurityoauth2social.model.Attributes;
import io.chan.springsecurityoauth2social.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoUser extends OAuth2ProviderUser {

    private static final String ID = "sub";
    private static final String EMAIL = "email";

    public static final String PROFILE_IMAGE = "picture";
    public static final String NICKNAME = "nickname";


    public KakaoUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return getAttributes().get(ID).toString();
    }

    @Override
    public String getUsername() {
        return getAttributes().get(NICKNAME).toString();
    }

    @Override
    public String getEmail() {
        return getAttributes().get(EMAIL).toString();
    }

    @Override
    public String getPicture() {
        return getAttributes().get(PROFILE_IMAGE).toString();
    }

}
