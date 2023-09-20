package io.start.demo.user.controller.response;

import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String address;
    private UserStatus status;
    private Long lastLoginAt;

    public static MyProfileResponse from(User user) {
        return MyProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .status(user.getStatus())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
