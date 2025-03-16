package io.chan.queuingsystemforjava.global.security;


public record TokenPair(
        String accessToken,
        String refreshToken
) {}
