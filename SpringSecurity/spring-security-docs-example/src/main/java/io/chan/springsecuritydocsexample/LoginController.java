package io.chan.springsecuritydocsexample;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;
//    private final SecurityContextRepository securityContextRepository;
//    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
//        this.securityContextHolderStrategy = securityContextHolderStrategy;
//        this.securityContextRepository = new HttpSessionSecurityContextRepository();
    }

    @GetMapping("/")
    public ResponseEntity<Void> index() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
            ) {
        UsernamePasswordAuthenticationToken authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationRequest);
//        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
//        context.setAuthentication(authenticate);
//        securityContextHolderStrategy.setContext(context);
//        securityContextRepository.saveContext(context, request, response);


        return ResponseEntity.ok().build();
    }
}
