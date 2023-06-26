package io.start.demo.security.service;

import io.start.demo.common.domain.exception.MyUsernameNotFoundException;
import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.security.controller.request.RegisterRequest;
import io.start.demo.security.controller.request.AuthenticationRequest;
import io.start.demo.security.controller.response.AuthenticationResponse;
import io.start.demo.user.domain.User;
import io.start.demo.user.infrastructure.UserEntity;
import io.start.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UuidHolder uuidHolder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.register(request, passwordEncoder, uuidHolder);
        user = userRepository.save(user);
        String jwtToken = jwtService.generateToken(UserEntity.from(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MyUsernameNotFoundException(request.getEmail()));
        String jwtToken = jwtService.generateToken(UserEntity.from(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
