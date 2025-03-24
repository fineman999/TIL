package io.chan.queuingsystemforjava.domain.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchException;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.dto.request.CreateMemberRequest;
import io.chan.queuingsystemforjava.domain.member.dto.response.CreateMemberResponse;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@Import({MemberRepository.class}) // 필요한 빈을 모두 등록
class MemberServiceTest {
  private MemberService memberService;

  @Autowired private MemberRepository memberRepository;
  private PasswordEncoder passwordEncoder;

  @TestConfiguration
  static class TestConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
      return new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
          return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
          return false; // 테스트용이므로 단순 구현
        }
      };
    }

    @Bean
    public MemberService memberService(
        MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
      return new MemberService(memberRepository, passwordEncoder);
    }
  }

  @BeforeEach
  void setUp() {
    passwordEncoder =
        new PasswordEncoder() {
          @Override
          public String encode(final CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
          }

          @Override
          public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
            return false;
          }
        };
    memberService = new MemberService(memberRepository, passwordEncoder);
  }

  @Nested
  @DisplayName("회원 생성 메서드 호출 시")
  class CreateMemberTest {

    @Test
    @DisplayName("회원을 생성한다.")
    void createMember() {
      // given
      String email = "email@email.com";
      String password = "password";
      CreateMemberRequest request = new CreateMemberRequest(email, password);

      // when
      CreateMemberResponse response = memberService.saveMember(request);

      // then
      assertThat(memberRepository.findById(response.memberId()))
          .isNotEmpty()
          .get()
          .satisfies(
              member -> {
                assertThat(member.getEmail()).isEqualTo(email);
                assertThat(member.getPassword()).isEqualTo(passwordEncoder.encode(password));
                assertThat(member.getMemberRole()).isEqualTo(MemberRole.USER);
              });
    }

    @Test
    @DisplayName("예외(duplicateResource): 중복된 이메일을 가진 회원이 있으면")
    void duplicateResource_WhenDuplicateEmail() {
      // given
      String duplicateEmail = "duplicate@email.com";
      String password = "password";
      Member member =
          Member.builder()
              .email(duplicateEmail)
              .password(password)
              .memberRole(MemberRole.USER)
              .build();
      memberRepository.save(member);

      CreateMemberRequest request = new CreateMemberRequest(duplicateEmail, password);

      // when
      Exception exception = catchException(() -> memberService.saveMember(request));

      // then
      assertThat(exception)
          .isInstanceOf(TicketingException.class)
          .extracting("errorCode")
          .isEqualTo(ErrorCode.DUPLICATED_EMAIL);
    }
  }
}
