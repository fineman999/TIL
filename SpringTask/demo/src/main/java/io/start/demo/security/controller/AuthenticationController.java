package io.start.demo.security.controller;

import io.start.demo.security.controller.request.RegisterRequest;
import io.start.demo.security.controller.request.AuthenticationRequest;
import io.start.demo.security.controller.response.AuthenticationResponse;
import io.start.demo.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "인증&인가(Authentication&Authorization)")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
       return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/preAuthorize")
    public ResponseEntity<String> preAuthorize(
            HttpServletRequest request
    ) {
        log.info("preAuthorize request: {}", request);
        return ResponseEntity.ok("preAuthorize");
    }

}
