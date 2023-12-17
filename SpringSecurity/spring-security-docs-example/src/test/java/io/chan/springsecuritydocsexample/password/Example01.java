package io.chan.springsecuritydocsexample.password;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class Example01 {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode("myPassword");
        String result2 = encoder.encode("myPassword");
        System.out.println(result);
        Assertions.assertThat(result).isNotEqualTo(result2); // salt 값이 다르기 때문에 매번 다른 결과가 나온다.
        assertTrue(encoder.matches("myPassword", result));
    }

    @Test
    public void argon2() {
        // Create an encoder with all the defaults
        Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String result = encoder.encode("myPassword");
        assertTrue(encoder.matches("myPassword", result));
    }

    @Test
    public void bytesEncryptor() {

        String key = KeyGenerators.string().generateKey();
        System.out.printf("key: %s\n", key);

        BytesEncryptor stronger = Encryptors.stronger("password", key);
        byte[] encrypt = stronger.encrypt("myPassword".getBytes());
        byte[] decrypt = stronger.decrypt(encrypt);
        System.out.println(new String(decrypt));

    }

    @Test
    public void bytesKeyGenerator() {
        BytesKeyGenerator bytesKeyGenerator = KeyGenerators.secureRandom();
        byte[] key = bytesKeyGenerator.generateKey();
        System.out.println(Arrays.toString(key));
    }

    @Test
    public void bytesKeyGenerator2() {
        BytesKeyGenerator bytesKeyGenerator = KeyGenerators.secureRandom(16);
        byte[] key = bytesKeyGenerator.generateKey();
        byte[] key2 = bytesKeyGenerator.generateKey();
        System.out.println(Arrays.toString(key));
        System.out.println(Arrays.toString(key2));
        Assertions.assertThat(key).isNotEqualTo(key2);
    }

    @Test
    public void bytesKeyGeneratorShared() {
        BytesKeyGenerator bytesKeyGenerator = KeyGenerators.shared(16);
        byte[] key = bytesKeyGenerator.generateKey();
        System.out.println(Arrays.toString(key));
        byte[] key2 = bytesKeyGenerator.generateKey();
        System.out.println(Arrays.toString(key2));
        Assertions.assertThat(key).isEqualTo(key2);
    }

    @Test
    public void stringKeyGenerator() {
        StringKeyGenerator stringKeyGenerator = KeyGenerators.string();
        String key = stringKeyGenerator.generateKey();
        System.out.println(key);
        String key2 = stringKeyGenerator.generateKey();
        System.out.println(key2);
        Assertions.assertThat(key).isNotEqualTo(key2);
    }

    @Test
    public void bcryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
        String encode = bCryptPasswordEncoder.encode("myPassword");
        System.out.println(encode);
        Assertions.assertThat(bCryptPasswordEncoder.matches("myPassword", encode)).isTrue();
    }
   @Test
    public void textEncryptor() {
        TextEncryptor password = Encryptors.text("password", "123456");
        String encrypt = password.encrypt("myPassword");
        String decrypt = password.decrypt(encrypt);
        System.out.println(decrypt);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder delegatingPasswordEncoder() {

            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
    }
}
