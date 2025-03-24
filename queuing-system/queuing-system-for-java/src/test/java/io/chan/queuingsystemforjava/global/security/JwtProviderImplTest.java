package io.chan.queuingsystemforjava.global.security;

import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.dto.response.CustomClaims;
import io.chan.queuingsystemforjava.domain.member.repository.MemberJpaRepository;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // JPA 관련 설정만 로드하여 테스트
class JwtProviderImplTest {
    private String issuer;
    private String secretKey;
    private JwtProvider jwtProvider;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    void setUp() {
        issuer = "test";
        int expirationSeconds = 3600;
        int refreshExpirationSeconds = 86400;
        secretKey = "thisisaverylongsecretkeyforjwtatleast32bytes!";
        jwtProvider = new JwtProviderImpl(issuer, expirationSeconds, refreshExpirationSeconds,secretKey);

    }

    @Nested
    @DisplayName("액세스 토큰 생성 메서드 실행 시")
    class CreateAccessToken {
        @Test
        @DisplayName("정상적으로 액세스 토큰을 생성한다.")
        void createAccessToken() {
            // given
            Member member =
                    Member.create("test@test.com", "password", MemberRole.USER);
            memberJpaRepository.save(member);

            // when
            String accessToken = jwtProvider.createAccessToken(member);

            // then
            assertThat(accessToken).isNotBlank();
        }
    }

    @Nested
    @DisplayName("액세스 토큰 파싱 메서드 실행 시")
    class ParseAccessToken {
        private Member member;
        private String accessToken;

        @BeforeEach
        void setUp() {
            member = Member.create("test@test.com", "password", MemberRole.USER);
            memberJpaRepository.save(member);

            accessToken = jwtProvider.createAccessToken(member);
        }

        @Test
        @DisplayName("정상적으로 액세스 토큰을 파싱한다.")
        void parseAccessToken() {
            // when
            CustomClaims customClaims = jwtProvider.parseAccessToken(accessToken);

            // then
            assertThat(customClaims.email()).isEqualTo(member.getEmail());
            assertThat(customClaims.memberRole()).isEqualTo(member.getMemberRole());
        }

        @Test
        @DisplayName("만료된 액세스 토큰을 파싱하면 예외가 발생한다.")
        void parseExpiredAccessToken() {
            // given
            jwtProvider = new JwtProviderImpl(issuer, -1,-1, secretKey);
            accessToken = jwtProvider.createAccessToken(member);

            Exception exception = catchException(() -> jwtProvider.parseAccessToken(accessToken));

            // then
            assertThat(exception).isInstanceOf(TicketingException.class)
                    .extracting("errorCode") // TicketingException의 error 필드를 가져옴
                    .isEqualTo(ErrorCode.EXPIRED_TOKEN);
        }

        @Test
        @DisplayName("유효하지 않은 액세스 토큰을 파싱하면 예외가 발생한다.")
        void parseInvalidAccessToken() {
            // given
            accessToken = "invalid_access_token";

            // when
            Exception exception = catchException(() -> jwtProvider.parseAccessToken(accessToken));

            // then
            assertThat(exception).isInstanceOf(TicketingException.class)
                    .extracting("errorCode") // TicketingException의 error 필드를 가져옴
                    .isEqualTo(ErrorCode.INVALID_TOKEN);
        }

    }
}