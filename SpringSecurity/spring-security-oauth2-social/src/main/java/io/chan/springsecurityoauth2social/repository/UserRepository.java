package io.chan.springsecurityoauth2social.repository;

import io.chan.springsecurityoauth2social.model.users.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}
