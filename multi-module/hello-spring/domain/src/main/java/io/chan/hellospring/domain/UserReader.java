package io.chan.hellospring.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User findById(final Long id) {
        return userRepository.findById(id);
    }
}
