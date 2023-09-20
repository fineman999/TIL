package io.start.demo.user.controller;


import io.start.demo.common.domain.utils.ApiUtils.ApiResult;
import io.start.demo.user.controller.port.UserService;
import io.start.demo.user.controller.response.MyProfileResponse;
import io.start.demo.user.controller.response.UserResponse;
import io.start.demo.user.domain.User;
import io.start.demo.user.domain.UserUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.start.demo.common.domain.utils.ApiUtils.success;

@Tag(name = "유저(users)")
@RestController
@RequestMapping("/api/v1/users")
@Builder
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserResponse>> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.        OK)
                .body(success(UserResponse.from(userService.getById(id))));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        userService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResult<MyProfileResponse>> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
    ) {
        User user = userService.getByEmail(email);
        userService.login(user.getId());
        user = userService.getByEmail(email);
        return ResponseEntity
            .ok()
            .body(success(MyProfileResponse.from(user)));
    }

    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<ApiResult<MyProfileResponse>> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져옵니다.
        @RequestBody UserUpdate userUpdate
    ) {
        User user = userService.getByEmail(email);
        user = userService.update(user.getId(), userUpdate);
        return ResponseEntity
            .ok()
            .body(success(MyProfileResponse.from(user)));
    }

}