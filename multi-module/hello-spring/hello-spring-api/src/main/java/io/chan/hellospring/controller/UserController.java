package io.chan.hellospring.controller;

import io.chan.hellospring.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody final UserRequest userRequest) {
    userService.save(userRequest.toDomain());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable final Long id) {
    return ResponseEntity.ok(UserResponse.fromDomain(userService.findById(id)));
  }
}
