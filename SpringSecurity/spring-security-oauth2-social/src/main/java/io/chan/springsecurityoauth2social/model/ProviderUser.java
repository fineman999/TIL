package io.chan.springsecurityoauth2social.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProviderUser {

    String getId();
    String getUsername();
    String getEmail();
    String getProvider(); // 어디 생성자인지 확인
    List<? extends GrantedAuthority> getAuthorities(); // 권한 확인
    Map<String, Object> getAttributes(); // 서비스 제공자로부터 받는 정보

    default String getPassword() {
        return UUID.randomUUID().toString();
    } // 랜덤으로 만들어서 의미는 없다.

}
