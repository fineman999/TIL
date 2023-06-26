package io.start.demo.security.controller.request;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterRequest {
    private String email;
    private String password;
    private String nickname;
}
