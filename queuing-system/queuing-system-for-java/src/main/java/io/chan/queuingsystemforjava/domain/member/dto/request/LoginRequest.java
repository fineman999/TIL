package io.chan.queuingsystemforjava.domain.member.dto.request;

import io.jsonwebtoken.lang.Assert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        @Email
        String email,
        @NotNull
        String password
) {
    public LoginRequest {
        Assert.hasText(email, "email must not be empty");
        Assert.hasText(password, "password must not be empty");
    }
}
