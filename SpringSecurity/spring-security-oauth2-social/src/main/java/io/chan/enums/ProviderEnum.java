package io.chan.enums;

import io.chan.functions.TriFunction;
import io.chan.springsecurityoauth2social.model.Attributes;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.social.GoogleUser;
import io.chan.springsecurityoauth2social.model.social.KakaoUser;
import io.chan.springsecurityoauth2social.model.social.KeycloakUser;
import io.chan.springsecurityoauth2social.model.social.NaverUser;
import io.chan.util.OAuth2Utils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;

/**
 * @link @BiFunction
 * Java에서 제공하는 함수형 인터페이스(Functional Interface) 중 하나로, 두 개의 인수를 받아서 결과를 반환하는 함수를 나타냄
 * T: 첫 번째 인수의 타입
 * U: 두 번째 인수의 타입
 * R: 결과의 타입
 */
public enum ProviderEnum {
    NAVER("naver", NaverUser::new, "response", ""),
    GOOGLE("google", GoogleUser::new, "", ""),
    KEYCLOAK("keycloak", KeycloakUser::new, "", ""),
    KAKAO("kakao", KakaoUser::new, "kakao_account", "profile");

    private final String registrationId;
    private final TriFunction<Attributes, OAuth2User, ClientRegistration, ProviderUser> userFactory;
    private final String subAttributeName;
    private final String lastAttributeName;

    ProviderEnum(
            String registrationId,
            TriFunction<Attributes, OAuth2User, ClientRegistration, ProviderUser> userFactory,
            String subAttributeName,
            String lastAttributeName) {
        this.registrationId = registrationId;
        this.userFactory = userFactory;
        this.subAttributeName = subAttributeName;
        this.lastAttributeName = lastAttributeName;
    }

    public static ProviderUser newProviderUser(
            String registrationId,
            OAuth2User oAuth2User,
            ClientRegistration clientRegistration
    ) {
        return Arrays.stream(values())
            .filter(provider -> provider.registrationId.equals(registrationId))
            .findFirst()
            .map(provider -> {
                Attributes attributes = OAuth2Utils.getAttributes(
                        oAuth2User,
                        provider.subAttributeName,
                        provider.lastAttributeName
                );

                return provider.userFactory.apply(
                        attributes,
                        oAuth2User,
                        clientRegistration
                );
            }
            )
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 OAuth2 인증 서버입니다."));
    }

}
