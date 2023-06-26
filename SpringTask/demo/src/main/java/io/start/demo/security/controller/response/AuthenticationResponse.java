package io.start.demo.security.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationResponse {

    private String token;
}
