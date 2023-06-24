package io.start.demo.user.service;


import io.start.demo.common.domain.exception.ResourceNotFoundException;
import io.start.demo.common.service.port.ClockHolder;
import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.user.controller.port.UserService;
import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserCreate;
import io.start.demo.user.domain.UserStatus;
import io.start.demo.user.domain.UserUpdate;
import io.start.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Builder
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CertificationService certificationService;

    private final UuidHolder uuidHolder;

    private final ClockHolder clockHolder;
    /*
    사용하지 않으면 원래는 지움 - 공부용
    public Optional<User> findById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
    }
    */

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }
    @Override
    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Transactional
    @Override
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());
        return user;
    }

    @Transactional
    @Override
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        user = user.update(userUpdate);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login(clockHolder);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.certificate(certificationCode, user.getCertificationCode());
        userRepository.save(user);
    }

}