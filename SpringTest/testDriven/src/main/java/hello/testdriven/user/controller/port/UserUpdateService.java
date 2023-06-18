package hello.testdriven.user.controller.port;

import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserUpdate;

public interface UserUpdateService {

    User update(long id, UserUpdate userUpdate);
}
