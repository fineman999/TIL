package io.chan.util;

import io.chan.springsecurityoauth2social.model.Attributes;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

public final class OAuth2Utils {

    private OAuth2Utils() {
        throw new IllegalStateException("Utility class");
    }


    public static Attributes getAttributes(
            OAuth2User oAuth2User,
            String subAttributesKey,
            String otherAttributesKey
    ) {

        if (Objects.isNull(oAuth2User.getAttributes().get(subAttributesKey)) &&
                Objects.isNull(oAuth2User.getAttributes().get(subAttributesKey))) {
            return getMainAttributes(oAuth2User);
        }

        if (Objects.isNull(oAuth2User.getAttributes().get(otherAttributesKey))) {
            return getSubAttributes(oAuth2User, subAttributesKey);
        }

        return getOtherAttributes(oAuth2User, subAttributesKey, otherAttributesKey);
    }

    private static Attributes getMainAttributes(OAuth2User oAuth2User) {

        return Attributes.builder()
                .mainAttributes(oAuth2User.getAttributes())
                .build();
    }

    /**
     * OAuth2 공급자로부터 받은 사용자 정보 중 중간 정보를 담는 클래스
     * 계층이 두 개인 경우에 사용
     * ex) Naver
     */
    @SuppressWarnings("unchecked")
    private static Attributes getSubAttributes(OAuth2User oAuth2User, String subAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        return Attributes.builder()
                .subAttributes(subAttributes)
                .build();
    }

    /**
     * OAuth2 공급자로부터 받은 사용자 정보 중 마지막 정보를 담는 클래스
     * 계층이 세 개 이상인 경우에 사용
     * ex) Kakao
     */
    @SuppressWarnings("unchecked")
    private static Attributes getOtherAttributes(OAuth2User oAuth2User, String subAttributesKey, String otherAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        Map<String, Object> otherAttributes = (Map<String, Object>) subAttributes.get(otherAttributesKey);

        return Attributes.builder()
                .subAttributes(subAttributes)
                .otherAttributes(otherAttributes)
                .build();
    }
}
