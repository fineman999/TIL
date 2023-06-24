package io.start.demo.user.service;

import io.start.demo.mock.FakeMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CertificationServiceTest {

    @Test
    @DisplayName("이메일과 컨텐츠가 제대로 만들어져서 보내지는지 테스트한다.")
    void send() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // when
        certificationService.send("spring2@naver.com", 1L, "aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(fakeMailSender.email).isEqualTo("spring2@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }
}