package io.chan.springsecurityoauth2social.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2 공급자로부터 받은 사용자 정보를 담는 클래스
 * OAuth2 공급자로부터 받은 사용자 정보는 Map 형태로 제공되기 때문에
 * Map 형태의 사용자 정보를 담는 클래스가 필요함
 * <p>
 *     공급자 마다 제공하는 사용자 정보의 형태가 다르기 때문에
 *     Map 형태의 사용자 정보를 담는 클래스도 공급자 마다 다르게 구현해야 함
 * <p>
 *     mainAttributes: 공급자로부터 받은 사용자 정보 중 필수 정보
 *     subAttributes: 공급자로부터 받은 사용자 정보 중 중간 정보
 *     otherAttributes: 공급자로부터 받은 사용자 정보 중 마지막 정보
 *     (공급자 마다 제공하는 사용자 정보의 형태가 다르기 때문에 필요한 경우에만 사용)
 */
@Getter
public class Attributes {

    private final Map<String, Object> mainAttributes;
    private final Map<String, Object> subAttributes;
    private final Map<String, Object> otherAttributes;

    @Builder
    public Attributes(Map<String, Object> mainAttributes, Map<String, Object> subAttributes, Map<String, Object> otherAttributes) {
        this.mainAttributes = mainAttributes;
        this.subAttributes = subAttributes;
        this.otherAttributes = otherAttributes;
    }
}
