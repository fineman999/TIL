package io.chan.springsecurityresources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MacTest {

    @Test
    @DisplayName("Mac 클래스를 사용하여 HMAC을 생성한다.")
    void hmac() throws NoSuchAlgorithmException, InvalidKeyException {
        String data = "Spring Security OAuth2";

        hmacBase64("secretKey", data, "HmacMD5");
        hmacBase64("secretKey", data, "HmacSHA256");
    }

    void hmacBase64(String secret, String data, String algorithms) throws NoSuchAlgorithmException, InvalidKeyException {

        // SecretKeySpec 클래스는 비밀키를 생성하기 위한 클래스이다.
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithms);

        // Mac 클래스는 메시지 인증 코드를 생성하기 위한 클래스이다.
        Mac mac = Mac.getInstance(algorithms);

        // init 메서드는 비밀키를 초기화한다.
        mac.init(secretKey);

        // doFinal 메서드는 전달된 바이트 배열을 사용하여 메시지 인증 코드를 생성한다.
        byte[] hash = mac.doFinal(data.getBytes());

        // Base64 클래스는 바이트 배열을 Base64로 인코딩한다.
        String encodedStr = Base64.getEncoder().encodeToString(hash);

        //
        System.out.println(algorithms + ": " + encodedStr);
    }
}
