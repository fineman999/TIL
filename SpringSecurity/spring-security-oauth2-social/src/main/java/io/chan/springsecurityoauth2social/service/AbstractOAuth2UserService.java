package io.chan.springsecurityoauth2social.service;

import io.chan.springsecurityoauth2social.converters.ProviderUserConverter;
import io.chan.springsecurityoauth2social.converters.ProviderUserRequest;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OAuth2와 관련된 서비스의 공통 기능을 구현한 추상 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;


    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }

    protected void register(ProviderUser providerUser) {

        userRepository.findByUsername(providerUser.getUsername())
                .ifPresentOrElse(user -> {
                    // 이미 가입된 회원이면 아무 작업도 하지 않음
                    log.info("이미 가입된 회원입니다. username: {}", user.getUsername());
                }, () -> {
                    // 가입되지 않은 회원이면 회원 가입
                    userService.save(providerUser);
                });
    }
}
