package io.chan.springsecurityoauth2social.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * @link @BiFunction
 * Java에서 제공하는 함수형 인터페이스(Functional Interface) 중 하나로, 두 개의 인수를 받아서 결과를 반환하는 함수를 나타냄
 * T: 첫 번째 인수의 타입
 * U: 두 번째 인수의 타입
 * R: 결과의 타입
 */
public enum ProviderEnum {
    NAVER("naver", NaverUser::new),
    GOOGLE("google", GoogleUser::new),
    KEYCLOAK("keycloak", KeycloakUser::new);

    private final String registrationId;
    private final BiFunction<OAuth2User, ClientRegistration, ProviderUser> userFactory;

    ProviderEnum(String registrationId, BiFunction<OAuth2User, ClientRegistration, ProviderUser> userFactory) {
        this.registrationId = registrationId;
        this.userFactory = userFactory;
    }

    public static ProviderUser newProviderUser(String registrationId, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        return Arrays.stream(values())
                .filter(provider -> provider.registrationId.equals(registrationId))
                .findFirst()
                .map(provider -> provider.userFactory.apply(oAuth2User, clientRegistration))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 OAuth2 인증 서버입니다."));
    }

}