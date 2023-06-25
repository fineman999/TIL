package io.start.demo.user.controller.port;

import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserCreate;
import io.start.demo.user.domain.UserUpdate;

public interface UserService {
    User getByEmail(String email);
    User getById(long id);
    User create(UserCreate userCreate);
    User update(long id, UserUpdate userUpdate);
    void login(long id);
    void verifyEmail(long id, String certificationCode);
}
