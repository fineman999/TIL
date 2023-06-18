package hello.testdriven.user.service;


import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.common.service.port.ClockHolder;
import hello.testdriven.common.service.port.UuidHolder;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import hello.testdriven.user.domain.UserCreate;
import hello.testdriven.user.domain.UserUpdate;
import hello.testdriven.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

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

    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());
        return user;
    }

    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        user = user.update(userUpdate);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login(clockHolder);
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.certificate(certificationCode, user.getCertificationCode());
        userRepository.save(user);
    }

}