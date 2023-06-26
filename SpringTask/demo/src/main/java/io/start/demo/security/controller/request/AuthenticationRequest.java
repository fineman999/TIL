package io.start.demo.security.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationRequest {

        private String email;
        private String password;
}
