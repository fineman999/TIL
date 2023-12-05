package io.chan.springsecurityresources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDigestTest {

    String absolutePath;
    @BeforeEach
    void setUp() {
        String relativePath = "src/test/resources/message.txt";
        absolutePath = getResource(relativePath);
    }

    @Test
    @DisplayName("MessageDigest 클래스를 사용하여 MD5 다이제스트를 생성과 인증을 한다..")
    void messageDigest() throws Exception {
        String message = "Spring Security OAuth2";
        // createMD5 메서드는 전달된 메시지를 사용하여 다이제스트를 생성한다.
        createMD5(message);
        // validateMD5 메서드는 전달된 메시지를 사용하여 다이제스트를 인증한다.
        validateMD5(message);
    }

    void createMD5(String message) throws Exception {

        // SecureRandom 클래스는 암호학적으로 안전한 난수를 생성한다.
        SecureRandom random = new SecureRandom();

        // salt는 다이제스트를 생성할 때 사용하는 임의의 바이트 배열이다.
        byte[] salt = new byte[10];
        // SecureRandom 클래스의 nextBytes 메서드는 매개변수로 전달된 바이트 배열에 난수를 채운다.
        random.nextBytes(salt);

        // MessageDigest 클래스는 암호학적 해시 함수를 구현한다.
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // update 메서드는 전달된 바이트 배열을 사용하여 다이제스트를 갱신한다.
        messageDigest.update(salt);

        // update 메서드는 전달된 메시지 바이트 배열을 사용하여 다이제스트를 갱신한다.
        messageDigest.update(message.getBytes(StandardCharsets.UTF_16));

        // digest 메서드는 다이제스트를 생성한다.
        byte[] digest = messageDigest.digest();

        // FileOutputStream 클래스는 파일에 바이트를 쓰기 위한 클래스이다.
        FileOutputStream fileOutputStream = new FileOutputStream(absolutePath);
        fileOutputStream.write(salt);
        fileOutputStream.write(digest);
        fileOutputStream.close();
    }

    void validateMD5(String message) throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(absolutePath);
        int theByte = 0;
        while ((theByte = fis.read()) != -1)
            byteArrayOutputStream.write(theByte);
        fis.close();
        byte[] hashedMessage = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.reset();

        // 10바이트 만큼의 salt를 구한다.
        byte[] salt = new byte[10];
        System.arraycopy(hashedMessage, 0, salt, 0, 10);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(message.getBytes(StandardCharsets.UTF_16));
        byte[] digest = md.digest();

        // salt값을 제외한 10바이트 이후의 digest를 구한다.
        byte[] digestInFile = new byte[hashedMessage.length - 10];
        System.arraycopy(hashedMessage, 10, digestInFile, 0, hashedMessage.length - 10);


        // 을이 계산한 메시지 해시 값과 갑이 전달한 메시지 해시 값이 일치하면,
        // 갑이 전달한 원본이 네트워크를 통해 을에게 오기까지 변경되지 않았다는 것을 확인할 수 있다.
        assertThat(Arrays.equals(digest, digestInFile)).isTrue();
    }

    private String getResource(String relativePath) {
        String path = MessageDigestTest.class.getResource("").getPath();
        String[] split = path.split("/build/classes/java/test/io/chan/springsecurityresources");
        return split[0] + "/" + relativePath;
    }


}
