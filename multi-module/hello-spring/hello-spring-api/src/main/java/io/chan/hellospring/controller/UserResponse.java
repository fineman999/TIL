package io.chan.hellospring.controller;

import io.chan.hellospring.domain.User;

record UserResponse(
    Long id,
    String name
) {
    public static UserResponse fromDomain(final User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
