package hello.testdriven.user.controller.port;

import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserCreate;
import hello.testdriven.user.domain.UserUpdate;

public interface UserService {
    User getByEmail(String email);
    User getById(long id);
    User create(UserCreate userCreate);
    User update(long id, UserUpdate userUpdate);
    void login(long id);
    void verifyEmail(long id, String certificationCode);
}
