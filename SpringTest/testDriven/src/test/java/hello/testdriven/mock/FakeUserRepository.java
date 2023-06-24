package hello.testdriven.mock;

import hello.testdriven.common.domain.exception.ResourceNotFoundException;
import hello.testdriven.user.domain.User;
import hello.testdriven.user.domain.UserStatus;
import hello.testdriven.user.service.port.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<User> data = new ArrayList<>();

    @Override
    public User getById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny().orElseThrow(()->
                new ResourceNotFoundException("Posts", id));
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus active) {
        return data.stream().filter(item->item.getEmail().equals(email) && item.getStatus() == active).findFirst();
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus active) {
        return data.stream().filter(item->item.getId().equals(id) && item.getStatus() == active).findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .status(user.getStatus())
                    .certificationCode(user.getCertificationCode())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny();
    }
}