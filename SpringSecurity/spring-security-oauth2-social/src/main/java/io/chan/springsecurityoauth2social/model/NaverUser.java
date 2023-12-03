package io.chan.springsecurityoauth2social.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUser extends OAuth2ProviderUser {

    private static final String ID = "id";
    private static final String USERNAME = "email";

    /**
     * Naver OAuth 같은 경우 response 객체에 담겨져 있다.
     * 그러므로 response 객체를 받아서 Map으로 형변환을 해준다.
     */
    @SuppressWarnings("unchecked")
    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super( (Map<String, Object>) oAuth2User.getAttributes().get("response"), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return getAttributes().get(ID).toString();
    }

    @Override
    public String getUsername() {
        return getAttributes().get(USERNAME).toString();
    }
}
