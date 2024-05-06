package io.chan.hellospring.controller;

import io.chan.hellospring.domain.User;

record UserRequest(
    String name
) {

    public User toDomain() {
        return User.of(null, name);
    }
}
