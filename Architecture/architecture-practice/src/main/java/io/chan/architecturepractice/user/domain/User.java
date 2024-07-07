package io.chan.architecturepractice.user.domain;

import lombok.Builder;

public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    @Builder
    public User(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User CreateUser(final Long id, final String name, final String email, final String password) {
        return User.builder()
            .id(id == null ? 1L : id)
            .name(name)
            .email(email)
            .password(password)
            .build();
    }
}
