package io.chan.queuingsystemforjava.domain.waitingsystem.aop;


import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.domain.waitingsystem.running.RedisRunningRoom;
import io.chan.queuingsystemforjava.domain.waitingsystem.waiting.RedisWaitingLine;
import io.chan.queuingsystemforjava.global.config.WaitingConfig;
import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Import(WaitingConfig.class)
class WaitingAspectTest extends BaseIntegrationTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisRunningRoom runningRoom;

    @Autowired
    private RedisWaitingLine waitingLine;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().commands().flushAll();
    }

    private String getBearerToken(Member member) {
        return "Bearer " + jwtProvider.createAccessToken(member);
    }

    @Nested
    @DisplayName("대기열 시스템 AOP 적용 시")
    class WaitingSystemAop {

        @Nested
        @DisplayName("사용자가 작업 가능 공간에 없으면")
        class WhenRunningRoomNotContainsMember {

            private Member member;
            private String bearerToken;
            private long performanceId;

            @BeforeEach
            void setUp() {
                member =
                        Member.builder()
                                .email("email@email.com")
                                .password("asdfasdf")
                                .memberRole(MemberRole.USER)
                                .build();
                entityManager.persist(member);
                entityManager.flush();
                bearerToken = getBearerToken(member);
                performanceId = 1;
            }

            @Test
            @DisplayName("리다이렉트한다.")
            void redirect() throws Exception {
                // given

                // when
                ResultActions result =
                        mockMvc.perform(
                                get("/api/v1/waiting/test")
                                        .header("performanceId", performanceId)
                                        .header(AUTHORIZATION_HEADER, bearerToken));

                // then
                result.andExpect(status().isTemporaryRedirect());
            }

            @Test
            @DisplayName("대기열에 추가한다.")
            void enterWaitingLine() throws Exception {
                // given

                // when
                ResultActions result =
                        mockMvc.perform(
                                get("/api/v1/waiting/test")
                                        .header("performanceId", performanceId)
                                        .header(AUTHORIZATION_HEADER, bearerToken));

                // then
                assertThat(waitingLine.pullOutMembers(performanceId, 1))
                        .hasSize(1)
                        .first()
                        .satisfies(email -> assertThat(email).isEqualTo(member.getEmail()));
            }
        }

        @Test
        @DisplayName("사용자가 작업 가능 공간에 있으면 본래의 기능을 실행한다.")
        void proceed_WhenRunningRoomContainsMember() throws Exception {
            // given
            long performanceId = 1;
            Member member =
                    Member.builder()
                            .email("email@email.com")
                            .password("asdfasdf")
                            .memberRole(MemberRole.USER)
                            .build();
            String bearerToken = getBearerToken(member);
            entityManager.persist(member);
            entityManager.flush();
            runningRoom.enter(performanceId, Set.of(member.getEmail()));

            // when
            ResultActions result =
                    mockMvc.perform(
                            get("/api/v1/waiting/test")
                                    .header("performanceId", performanceId)
                                    .header(AUTHORIZATION_HEADER, bearerToken));

            // then
            result.andExpect(status().isOk());
        }
    }
}
