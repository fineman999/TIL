package io.chan.springsecurityoauth2v2.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class AppConfig {

    @Bean
    public DefaultOAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository
    ) {

        OAuth2AuthorizedClientProvider clientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .password()
                .clientCredentials()
                .refreshToken()
                .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientRepository
        );

        authorizedClientManager.setAuthorizedClientProvider(clientProvider);
        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper -> {
            Map<String, Object> contextAttributes = new HashMap<>();
            HttpServletRequest request = contextAttributesMapper.getAttribute(HttpServletRequest.class.getName());
            String username = Objects.requireNonNull(request).getParameter(OAuth2ParameterNames.USERNAME);
            String password = request.getParameter(OAuth2ParameterNames.PASSWORD);

            if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
                contextAttributes.put(OAuth2ParameterNames.USERNAME, username);
                contextAttributes.put(OAuth2ParameterNames.PASSWORD, password);
            }
            return contextAttributes;
        });
        return authorizedClientManager;
    }
}
