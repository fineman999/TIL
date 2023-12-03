package io.chan.springsecurityoauth2social.service;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService;

    public CustomOidcUserService(UserService userService, UserRepository userRepository, OidcUserService oidcUserService) {
        super(userService, userRepository);
        this.oidcUserService = oidcUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // OAuth2UserRequest 객체를 이용하여 OAuth2User 객체를 생성
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        ProviderUser providerUser = super.providerUser(clientRegistration, oidcUser);

        // 회원 가입
        super.register(providerUser);

        return oidcUser;
    }
}
