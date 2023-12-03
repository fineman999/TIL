package io.chan.springsecurityoauth2social.service;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * @link OAuth2UserService
 * @link OAuth2UserRequest:
 * OAuth 2.0 클라이언트에서 OAuth 2.0 인증 서버로 사용자 정보를 요청하는 데 필요한 정보를 캡슐화한 객체
 * 이 객체를 통해 클라이언트 등록 정보, 액세스 토큰 등을 얻는다.
 *
 * @link OAuth2User:
 * OAuth 2.0 인증 서버로부터 받은 사용자 정보를 나타내는 객체
 * 이는 Spring Security에서 사용되는 Principal 객체를 확장한 것으로, 사용자의 ID, 권한 등을 포함한다.
 * <p>
 * 따라서 OAuth2UserService<OAuth2UserRequest, OAuth2User>를 구현하는 서비스는
 * loadUser 메서드에서 주어진 OAuth2UserRequest를 이용하여
 * OAuth 2.0 인증 서버에서 사용자 정보를 가져와서 OAuth2User 객체로 변환해야 함.
 * <p>
 * 예를 들어, Google 또는 Facebook과 같은 OAuth 2.0 인증 서버로부터 받은 ID Token을 기반으로 사용자 정보를 추출하고,
 * 이 정보를 사용하여 OAuth2User 객체를 생성하는 등의 작업을 수행할 수 있다.
 * 구체적인 구현은 각각의 OAuth 2.0 프로바이더와 애플리케이션의 요구에 따라 달라진다.
 */
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;

    public CustomOAuth2UserService(UserService userService, UserRepository userRepository, DefaultOAuth2UserService defaultOAuth2UserService) {
        super(userService, userRepository);
        this.defaultOAuth2UserService = defaultOAuth2UserService;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // OAuth2UserRequest 객체를 이용하여 OAuth2User 객체를 생성
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        ProviderUser providerUser = super.providerUser(clientRegistration, oAuth2User);

        // 회원 가입
        super.register(providerUser);

        return oAuth2User;
    }
}
