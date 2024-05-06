package io.chan.hellospring.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserWriter userWriter;
  private final UserReader userReader;

  public User findById(final Long id) {
    return userReader.findById(id);
  }
  public void save(final User user) {
    userWriter.save(user);
  }
}
