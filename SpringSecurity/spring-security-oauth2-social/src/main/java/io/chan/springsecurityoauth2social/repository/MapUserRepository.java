package io.chan.springsecurityoauth2social.repository;

import io.chan.springsecurityoauth2social.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MapUserRepository implements UserRepository {

    private final Map<String, User> users;

    public MapUserRepository() {
        this.users = new HashMap<>();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (users.containsKey(username)) {
            return Optional.of(users.get(username));
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        users.putIfAbsent(user.getUsername(), user);
    }

}
