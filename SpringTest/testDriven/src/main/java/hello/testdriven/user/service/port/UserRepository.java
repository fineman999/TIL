package hello.testdriven.user.service.port;


import hello.testdriven.user.domain.UserStatus;
import hello.testdriven.user.infrastructure.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus active);

    Optional<UserEntity> findByIdAndStatus(long id, UserStatus active);

    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(long id);

}
