package io.start.demo.user.service.port;


import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {

    User getById(long id);

    Optional<User> findByEmailAndStatus(String email, UserStatus active);

    Optional<User> findByIdAndStatus(long id, UserStatus active);

    User save(User user);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

}
