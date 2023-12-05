package io.chan.springsecurityresources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;

public class SignatureTest {

    @Test
    @DisplayName("Signature 클래스를 사용하여 전자서명을 생성과 인증을 한다..")
    void signature() throws Exception {
        String message = "Spring Security OAuth2";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        byte[] data = message.getBytes("UTF-8");
        Signature signature = Signature.getInstance("SHA256WithRSA");
        // 서명 - private key를 사용하여 서명한다.
        signature.initSign(keyPair.getPrivate());
        signature.update(data);

        byte[] sign = signature.sign();
        // 검증  - public key를 사용하여 검증한다.
        signature.initVerify(keyPair.getPublic());
        signature.update(data);

        boolean verified = false;

        try {
            verified = signature.verify(sign);

        } catch (SignatureException e) {
            System.out.println("전자서명 실행 중 오류발생");
            e.printStackTrace();
        }
        assertThat(verified).isTrue();
    }
}
