package hello.testdriven.user.controller.port;

import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserCreate;

public interface UserCreateService {
    User create(UserCreate userCreate);
}
