package io.chan.springsecurityauthorization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegisteredClientController {

    private final RegisteredClientRepository registeredClientRepository;

    @GetMapping("/registered-clients")
    public List<RegisteredClient> getRegisteredClients() {
        RegisteredClient repositoryByClientId1 = registeredClientRepository.findByClientId("oauth2-client-app1");
        RegisteredClient repositoryByClientId2 = registeredClientRepository.findByClientId("oauth2-client-app2");
        RegisteredClient repositoryByClientId3 = registeredClientRepository.findByClientId("oauth2-client-app3");
        assert repositoryByClientId3 != null;
        assert repositoryByClientId2 != null;
        assert repositoryByClientId1 != null;
        return List.of(repositoryByClientId1, repositoryByClientId2, repositoryByClientId3);
    }
}
