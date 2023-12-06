package io.chan.springsecurityresources.configs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.resource")
public class KeyStoreProperties {
    private final Resource keyStore;
    private final String keyStorePassword;
    private final String keyPassword;
    private final String keyAlias;
}
