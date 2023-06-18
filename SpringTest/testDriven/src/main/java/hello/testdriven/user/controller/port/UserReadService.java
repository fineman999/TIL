package hello.testdriven.user.controller.port;

import hello.testdriven.user.domain.User;

public interface UserReadService {
    User getByEmail(String email);
    User getById(long id);
}
