package io.chan.springsecurityresources.configs;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.chan.springsecurityresources.signature.MacSecuritySigner;
import io.chan.springsecurityresources.signature.RsaSecuritySigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 서명과 관련된 설정을 담당하는 클래스
 */
@Configuration
public class SignatureConfig {

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
                .algorithm(JWSAlgorithm.RS256)
                .generate();
    }
}
