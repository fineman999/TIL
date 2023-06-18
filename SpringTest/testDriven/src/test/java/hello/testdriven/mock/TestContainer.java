package hello.testdriven.mock;

import hello.testdriven.common.service.port.ClockHolder;
import hello.testdriven.common.service.port.UuidHolder;
import hello.testdriven.post.controller.PostController;
import hello.testdriven.post.controller.PostCreateController;
import hello.testdriven.post.controller.port.PostService;
import hello.testdriven.post.service.PostServiceImpl;
import hello.testdriven.post.service.port.PostRepository;
import hello.testdriven.user.controller.UserController;
import hello.testdriven.user.controller.UserCreateController;
import hello.testdriven.user.controller.port.*;
import hello.testdriven.user.service.CertificationService;
import hello.testdriven.user.service.UserServiceImpl;
import hello.testdriven.user.service.port.MailSender;
import hello.testdriven.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;

    public final UserRepository userRepository;
    public final PostRepository postRepository;

    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;

    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostCreateController postCreateController;

    public final PostController postController;
    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.certificationService = new CertificationService(this.mailSender);
        this.postService = PostServiceImpl.builder()
                .postRepository(this.postRepository)
                .userRepository(this.userRepository)
                .clockHolder(clockHolder)
                .build();
        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService)
                .build();
        this.userReadService = userService;
        this.userCreateService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.userController = UserController.builder()
                .userReadService(userReadService)
                .userCreateService(userCreateService)
                .authenticationService(authenticationService)
                .userUpdateService(userUpdateService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(userCreateService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postService(postService)
                .build();
        this.postController = PostController.builder()
                .postService(postService)
                .build();
    }
}
