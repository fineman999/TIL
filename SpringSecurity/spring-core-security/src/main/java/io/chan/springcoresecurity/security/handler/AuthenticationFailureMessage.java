package io.chan.springcoresecurity.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

public enum AuthenticationFailureMessage {
    INVALID_CREDENTIALS("Invalid Username or Password", BadCredentialsException.class),
    INVALID_SECRET_KEY("Invalid Secret Key", InsufficientAuthenticationException.class);

    public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid Username or Password";
    private final String message;
    private final Class<? extends AuthenticationException> exceptionType;

    AuthenticationFailureMessage(String message, Class<? extends AuthenticationException> exceptionType) {
        this.message = message;
        this.exceptionType = exceptionType;
    }

    public static String from(AuthenticationException exception) {
        for (AuthenticationFailureMessage value : AuthenticationFailureMessage.values()) {
            if (value.exceptionType.isAssignableFrom(exception.getClass())) {
                return value.message;
            }
        }
        return INVALID_USERNAME_OR_PASSWORD;
    }
}

