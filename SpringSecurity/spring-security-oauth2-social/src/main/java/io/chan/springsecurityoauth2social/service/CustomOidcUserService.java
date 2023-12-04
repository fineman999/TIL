package io.chan.springsecurityoauth2social.service;

import io.chan.converters.ProviderUserRequest;
import io.chan.springsecurityoauth2social.model.PrincipalUser;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService;

    public CustomOidcUserService(
            UserService userService,
            UserRepository userRepository,
            OidcUserService oidcUserService,
            Function<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(userService, userRepository, providerUserConverter);
        this.oidcUserService = oidcUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // OAuth2UserRequest 객체를 이용하여 OAuth2User 객체를 생성
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = providerUser(providerUserRequest);

        // 회원 가입
        super.register(providerUser);

        return new PrincipalUser(providerUser);
    }
}
