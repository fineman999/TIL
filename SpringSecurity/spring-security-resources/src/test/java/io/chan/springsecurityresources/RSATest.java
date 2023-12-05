package io.chan.springsecurityresources;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class RSATest {

    @Test
    void rsa() throws Exception {
        String message = "Spring Security OAuth2";

        KeyPair keyPair = RSAGen.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // publicKey로 암호화
        String encrypted = RSAGen.encrypt(message, publicKey);

        // privateKey로 복호화
        String decrypted = RSAGen.decrypt(encrypted, privateKey);

        System.out.println("message : " + message);
        System.out.println("decrypted : " + decrypted);
        assertThat(message).isEqualTo(decrypted);

        // 키 스펙 전환하기
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);
        byte[] bytePrivateKey = privateKey.getEncoded();
        String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);

        // 키 스펙 전환하기

        // X.509 표준형식
        PublicKey X509PublicKey = RSAGen.getPublicKeyFromKeySpec(base64PublicKey);
        String encrypted2 = RSAGen.encrypt(message, X509PublicKey);
        String decrypted2 = RSAGen.decrypt(encrypted2, privateKey);

        System.out.println("message : " + message);
        System.out.println("decrypted2 : " + decrypted2);
        assertThat(message).isEqualTo(decrypted2);


        // PKCS8 표준형식
        PrivateKey PKCS8PrivateKey = RSAGen.getPrivateKeyFromKeySpec(base64PrivateKey);
        String decrypted3 = RSAGen.decrypt(encrypted2, PKCS8PrivateKey);

        System.out.println("message : " + message);
        System.out.println("decrypted3 : " + decrypted3);
        assertThat(message).isEqualTo(decrypted3);
    }
}