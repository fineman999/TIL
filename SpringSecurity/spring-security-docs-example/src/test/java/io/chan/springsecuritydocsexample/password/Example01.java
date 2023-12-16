package io.chan.springsecuritydocsexample.password;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Example01 {

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
}
