package io.chan.springsecurityresources;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class JWKTest {

    @Test
    @DisplayName("JWKSet 생성하기")
    void jwk() throws JOSEException, NoSuchAlgorithmException {

        // 비대칭키 JWK

        // 직접 생성하기
        KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        rsaKeyPairGenerator.initialize(2048);

        KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey1 = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("rsa-kid1")
                .build();


        // RSAKeyGenerator를 사용하여 생성하기
        RSAKey rsaKey2 = new RSAKeyGenerator(2048)
                .keyID("rsa-kid2")
                .keyUse(KeyUse.SIGNATURE)
                .keyOperations(Set.of(KeyOperation.SIGN))
                .algorithm(JWSAlgorithm.RS512)
                .generate();

        // 대칭키 JWK
        SecretKey secretKey = new SecretKeySpec(
                Base64.getDecoder().decode("bCzY/M48bbkwBEWjmNSIEPfwApcvXOnkCxORBEbPr+4="), "AES");

        // 빌더 패턴을 사용하여 생성하기
        OctetSequenceKey octetSequenceKey1 = new OctetSequenceKey.Builder(secretKey)
                .keyID("secret-kid1")
                .keyUse(KeyUse.SIGNATURE)
                .keyOperations(Set.of(KeyOperation.SIGN))
                .algorithm(JWSAlgorithm.HS256)
                .build();

        // OctetSequenceKeyGenerator를 사용하여 생성하기
        OctetSequenceKey octetSequenceKey2 = new OctetSequenceKeyGenerator(256)
                .keyID("secret-kid2")
                .keyUse(KeyUse.SIGNATURE)
                .keyOperations(Set.of(KeyOperation.SIGN))
                .algorithm(JWSAlgorithm.HS384)
                .generate();


        String kId;
        kId = rsaKey1.getKeyID();
//        kId = rsaKey2.getKeyID();
//        kId = octetSequenceKey1.getKeyID();
//        kId = octetSequenceKey2.getKeyID();

        JWSAlgorithm alg;
        alg = (JWSAlgorithm)rsaKey1.getAlgorithm();
//        alg = (JWSAlgorithm)rsaKey2.getAlgorithm();
//        alg = (JWSAlgorithm)octetSequenceKey1.getAlgorithm();
//        alg = (JWSAlgorithm)octetSequenceKey2.getAlgorithm();
//
        KeyType type;
        type = KeyType.RSA;
//        type = KeyType.OCT;

        jwkSet(kId,alg,type,rsaKey1,rsaKey2,octetSequenceKey1,octetSequenceKey2);
    }

    private void jwkSet(String kid, JWSAlgorithm alg,KeyType type,JWK ...jwk) throws KeySourceException {

        // jwtset에 여러개의 jwk를 담을 수 있다.
        JWKSet jwkSet = new JWKSet(List.of(jwk));
        JWKSource<SecurityContext> jwkSource =(jwkSelector, securityContext) -> jwkSelector.select(jwkSet);

        // JWKMatcher는 JWKSelector를 생성하기 위한 빌더 클래스이다.
        // 여러개의 jwk에서 해당 토큰에 맞는 jwk를 선택하기 위한 클래스이다.
        JWKMatcher jwkMatcher = new JWKMatcher.Builder()
                .keyType(type)
                .keyID(kid)
                .keyUses(KeyUse.SIGNATURE)
                .algorithms(alg)
                .build();

        // JWKSelector는 JWKSource에서 JWK를 선택하기 위한 클래스이다.
        JWKSelector jwkSelector = new JWKSelector(jwkMatcher);

        // JWKSource는 JWKSet에서 JWK를 선택하기 위한 클래스이다.
        List<JWK> jwks = jwkSource.get(jwkSelector, null);

        if(!jwks.isEmpty()){

            JWK jwk1 = jwks.get(0);

            KeyType keyType = jwk1.getKeyType();
            System.out.println("keyType = " + keyType);

            String keyID = jwk1.getKeyID();
            System.out.println("keyID = " + keyID);

            Algorithm algorithm = jwk1.getAlgorithm();
            System.out.println("algorithm = " + algorithm);

        }

        System.out.println("jwks = " + jwks);
    }
}
