package io.chan.springsecurityoauth2social.service;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.User;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 저장소와 연동하여 사용자 정보를 저장하는 서비스
 * <p>
 * @link OAuth2AuthenticationToken
 * 사용자의 인증 상태와 OAuth 2.0 프로토콜에서 얻은 액세스 토큰 및 기타 정보를 담고 있다.
 * getPrincipal(): 사용자의 주체(Principal)를 반환합니다. 주로 사용자의 ID나 유일한 식별자를 나타냅니다.
 * getAuthorities(): 사용자에게 부여된 권한을 반환합니다.
 * getDetails(): 토큰에 연결된 세부 정보를 반환합니다.
 * getAccessTokenValue(): OAuth 2.0 인증 서버에서 발급받은 액세스 토큰의 값을 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(ProviderUser providerUser) {
        User user = User.from(providerUser);
        userRepository.save(user);
    }


    /**
     * OAuth2User 객체에서 사용자의 이름을 추출하는 메서드
     * naver 프로바이더의 경우, 사용자의 이름이 "name"이 아닌 "response"라는 키로 저장되어 있기 때문에
     * 이를 처리하기 위해 추가로 분기를 처리하였다.
     */
    @SuppressWarnings("unchecked")
    public String getName(Authentication authentication, OAuth2User provider) {
        String name = "anonymous";
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        if (authenticationToken != null) {
            Map<String, Object> attributes = provider.getAttributes();
            name = attributes.get("name").toString();
            if (authenticationToken.getAuthorizedClientRegistrationId().equals("naver")) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                name = response.get("name").toString();
            }
        }
        return name;
    }
}
