package io.chan.springcoresecurity.service.impl;

import io.chan.springcoresecurity.domain.Account;
import io.chan.springcoresecurity.repository.UserRepository;
import io.chan.springcoresecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
