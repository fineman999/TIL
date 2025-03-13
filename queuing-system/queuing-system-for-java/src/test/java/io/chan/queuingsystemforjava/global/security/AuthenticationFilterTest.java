package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.repository.MemberJpaRepository;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.global.cache.CacheRepository;
import io.chan.queuingsystemforjava.global.cache.UserToken;
import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collection;

import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthenticationFilterTest extends BaseIntegrationTest {

    private MockFilterChain filterChain;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CacheRepository cacheRepository;

    @BeforeEach
    public void setup() {
        filterChain = new MockFilterChain();
    }

    @Nested
    @DisplayName("preHandle 호출 시")
    class PreHandle {
        MockHttpServletRequest request;
        MockHttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
        }

        @Nested
        @DisplayName("헤더에 토큰이 있을 경우")
        @Transactional
        class ContextWithToken {
            private Member member;
            private String accessToken;

            @BeforeEach
            void setUp() {
                member = Member.create("test@email.com", "password", MemberRole.USER);
                memberJpaRepository.save(member);
                accessToken = jwtProvider.createAccessToken(member);
                UserToken userToken = new UserToken(member.getMemberId(), member.getEmail(), member.getMemberRole().getValue());
                cacheRepository.saveTokenWithPrefix(accessToken, userToken, Duration.ofSeconds(3600));

            }

            @AfterEach
            void tearDown() {
                cacheRepository.remove(accessToken);
            }

            @Test
            @DisplayName("인증 프로세스를 진행한다.")
            void runAuthenticationProcess_WhenContainsAuthorizationHeader() throws Exception {
                // given
                String bearerAccessToken = "Bearer " + accessToken;
                request.addHeader("Authorization", bearerAccessToken);

                // when
                authenticationFilter.doFilterInternal(
                        request, response, filterChain);

                // then
                Authentication authentication =
                        SecurityContextHolder.getContext().getAuthentication();
                assertThat(authentication)
                        .isNotNull()
                        .isInstanceOf(Authentication.class) //
                        .satisfies(
                                auth -> {
                                    MemberContext principal = (MemberContext) auth.getPrincipal();
                                    Member user = principal.getUser();
                                    assertThat(user.getEmail()).isEqualTo(member.getEmail());
                                    Collection<GrantedAuthority> authorities = principal.getAuthorities();
                                    assertThat(authorities).matches(
                                            grantedAuthorities -> grantedAuthorities.stream()
                                                    .anyMatch(
                                                            grantedAuthority ->
                                                                    grantedAuthority.getAuthority()
                                                                            .equals(member.getMemberRole().getValue())));

                                });
            }

            @Test
            @DisplayName("예외(authentication): 액세스 토큰 형식이 Bearer가 아니면")
            void authentication_WhenAccessTokenTypeIsNotBearer() {
                // given
                String invalidAccessToken = "invalid" + accessToken;
                request.addHeader("Authorization", invalidAccessToken);

                // when
                Exception exception =
                        catchException(
                                () ->
                                        authenticationFilter.doFilterInternal(
                                                request,
                                                response,
                                                filterChain));

                // then
                assertThat(exception)
                        .isInstanceOf(TicketingException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.INVALID_TOKEN_HEADER);
            }
        }
    }
}