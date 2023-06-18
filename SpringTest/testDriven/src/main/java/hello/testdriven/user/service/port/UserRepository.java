package hello.testdriven.user.service.port;


import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmailAndStatus(String email, UserStatus active);

    Optional<User> findByIdAndStatus(long id, UserStatus active);

    User save(User user);

    Optional<User> findById(long id);

}
