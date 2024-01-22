package io.chan.appleinapptest.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "apple.service")
public class AppleProperties {
    private final Resource encodedKey;
    private final Resource appleIncRootCertificate;
    private final Resource appleComputerRootCertificate;
    private final Resource appleRootCAG2;
    private final Resource appleRootCAG3;
}
