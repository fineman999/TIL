package io.chan.springsecurityresources.configs;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.chan.springsecurityresources.signature.MacSecuritySigner;
import io.chan.springsecurityresources.signature.RsaPublicKeySecuritySigner;
import io.chan.springsecurityresources.signature.RsaSecuritySigner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * 서명과 관련된 설정을 담당하는 클래스
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KeyStoreProperties.class)
public class SignatureConfig {

    private final KeyStoreProperties keyStoreProperties;

    // 대칭키 암호화를 위한 빈 등록
    @Bean
    public MacSecuritySigner macSecuritySigner() {
        return new MacSecuritySigner();
    }

    @Bean
    public OctetSequenceKey octetSequenceKey() throws JOSEException {
        return new OctetSequenceKeyGenerator(256)
                .keyID("mac-key")
                .algorithm(JWSAlgorithm.HS256)
                .generate();
    }

    // 비대칭키 암호화를 위한 빈 등록
    @Bean
    public RsaSecuritySigner rsaSecuritySigner() {
        return new RsaSecuritySigner();
    }

    @Bean
    public RSAKey rsaKey() throws JOSEException {
        return new RSAKeyGenerator(2048)
                .keyID("rsa-key")
                .algorithm(JWSAlgorithm.RS512)
                .generate();
    }

    @Bean
    public RsaPublicKeySecuritySigner rsaPublicKeySecuritySigner() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());


        keystore.load(
                keyStoreProperties.getKeyStore().getInputStream(),
                keyStoreProperties.getKeyStorePassword().toCharArray()
        );


        String alias = keyStoreProperties.getKeyAlias();
        Key key = keystore.getKey(alias, keyStoreProperties.getKeyPassword().toCharArray());

        Certificate certificate = keystore.getCertificate(alias);
        PublicKey publicKey = certificate.getPublicKey();
        KeyPair keyPair = new KeyPair(publicKey, (PrivateKey) key);
        return new RsaPublicKeySecuritySigner(keyPair.getPrivate());

    }
}
