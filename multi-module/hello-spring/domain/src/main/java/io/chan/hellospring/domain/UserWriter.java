package io.chan.hellospring.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWriter {
    private final UserRepository userRepository;
    public void save(final User user) {
        userRepository.save(user);
    }
}
