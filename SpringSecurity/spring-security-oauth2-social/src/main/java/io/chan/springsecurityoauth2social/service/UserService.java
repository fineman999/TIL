package io.chan.springsecurityoauth2social.service;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.User;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 저장소와 연동하여 사용자 정보를 저장하는 서비스
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(ProviderUser providerUser) {
        User user = User.from(providerUser);
        userRepository.save(user);
    }
}
