package io.start.demo.common.domain.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUsernameNotFoundException extends UsernameNotFoundException {

    public MyUsernameNotFoundException(String message) {
        super(message + "를 찾을 수 없습니다.");
    }

    public MyUsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
