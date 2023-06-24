package io.start.demo.mock;
import io.start.demo.common.service.port.ClockHolder;
import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.user.controller.UserController;
import io.start.demo.user.controller.UserCreateController;
import io.start.demo.user.controller.port.UserService;
import io.start.demo.user.service.CertificationService;
import io.start.demo.user.service.UserServiceImpl;
import io.start.demo.user.service.port.MailSender;
import io.start.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;

    public final UserRepository userRepository;


    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;


    public final UserService userService;
    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.certificationService = new CertificationService(this.mailSender);

        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService)
                .build();
        this.userService = userService;
        this.userController = UserController.builder()
                .userService(userService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userService(userService)
                .build();
    }
}
