package io.chan.appleinapptest.utils;

import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AppleProperties.class)
public class AppStoreServerConfig {
    private final AppleUtils appleUtils;
    private final AppleProperties appleProperties;

    @Bean
    public AppStoreServerAPIClient appStoreServerAPIClient() {
        String issuerId = appleUtils.getIssuerId();
        String keyId = appleUtils.getKeyId();
        String bundleId = appleUtils.getBundleId();

        String encodedKey;
        try {
            encodedKey = new String(appleProperties.getEncodedKey().getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("encodedKey error", e);
            throw new RuntimeException(e);
        }
        Environment environment = Environment.SANDBOX;

        return new AppStoreServerAPIClient(encodedKey, keyId, issuerId, bundleId, environment);
    }

    @Bean
    public SignedDataVerifier signedDataVerifier() {
        try {
            Set<InputStream> rootCAs = getInputStreams();

            boolean onlineChecks = true;
            Environment environment = Environment.SANDBOX;
            return new SignedDataVerifier(
                    rootCAs,
                    appleUtils.getBundleId(),
                    appleUtils.getAppleId(),
                    environment,
                    onlineChecks);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<InputStream> getInputStreams() throws IOException {
        InputStream appleIncRootCertificate = appleProperties.getAppleIncRootCertificate().getInputStream();
        InputStream appleComputerRootCertificate = appleProperties.getAppleComputerRootCertificate().getInputStream();
        InputStream appleRootCAG2 = appleProperties.getAppleRootCAG2().getInputStream();
        InputStream appleRootCAG3 = appleProperties.getAppleRootCAG3().getInputStream();
        return Set.of(
                appleIncRootCertificate,
                appleComputerRootCertificate,
                appleRootCAG2,
                appleRootCAG3
        );
    }

}
