package io.start.demo.security.controller;

import io.start.demo.common.domain.utils.ApiUtils.ApiResult;
import io.start.demo.security.controller.request.RegisterRequest;
import io.start.demo.security.controller.request.AuthenticationRequest;
import io.start.demo.security.controller.response.AuthenticationResponse;
import io.start.demo.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.start.demo.common.domain.utils.ApiUtils.success;


@Slf4j
@Tag(name = "인증&인가(Authentication&Authorization)")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public  ResponseEntity<ApiResult<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(success(authenticationService.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResult<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
       return ResponseEntity.ok(success(authenticationService.authenticate(request)));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/preAuthorize")
    public ResponseEntity<String> preAuthorize(
            HttpServletRequest request
    ) {
        log.info("preAuthorize request: {}", request);
        return ResponseEntity.ok("preAuthorize");
    }

}
