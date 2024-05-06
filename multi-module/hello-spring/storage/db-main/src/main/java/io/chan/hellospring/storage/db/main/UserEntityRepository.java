package io.chan.hellospring.storage.db.main;

import io.chan.hellospring.domain.User;
import io.chan.hellospring.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserEntityRepository implements UserRepository {
  private final UserJpaRepository userJpaRepository;

  @Override
  public User findById(final Long id) {
    return userJpaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found"))
        .entityToDomain();
  }

  @Override
  public void save(final User user) {
        userJpaRepository.save(UserEntity.fromDomain(user));
  }
}
