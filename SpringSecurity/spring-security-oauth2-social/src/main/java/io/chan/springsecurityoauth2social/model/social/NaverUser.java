package io.chan.springsecurityoauth2social.model.social;

import io.chan.springsecurityoauth2social.model.Attributes;
import io.chan.springsecurityoauth2social.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class NaverUser extends OAuth2ProviderUser {

    private static final String ID = "id";
    private static final String USERNAME = "email";

    public static final String USER_NAME_ATTRIBUTE_NAME = "response";

    /**
     * Naver OAuth 같은 경우 response 객체에 담겨져 있다.
     * 그러므로 response 객체를 받아서 Map으로 형변환을 해준다.
     */
    public NaverUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getSubAttributes(), oAuth2User, clientRegistration, USER_NAME_ATTRIBUTE_NAME);
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
    public String getEmail() {
        return getAttributes().get(USERNAME).toString();
    }

}
