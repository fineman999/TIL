package io.chan.queuingsystemforjava.domain.waitingsystem.listener;

import io.chan.queuingsystemforjava.common.event.EventPublisher;
import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class WaitingSystemEventListenerTest extends BaseIntegrationTest {

    @Autowired private WaitingSystem waitingSystem;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired private StringRedisTemplate redisTemplate;

    private ZSetOperations<String, String> rawRunningRoom;

    @BeforeEach
    void setUp() {
        rawRunningRoom = redisTemplate.opsForZSet();
        redisTemplate.getConnectionFactory().getConnection().commands().flushAll();
    }

    private String getRunningRoomKey(long performanceId) {
        return "running_room::" + performanceId;
    }

    @Nested
    @DisplayName("폴링 이벤트 발행 시")
    class PublishPoolingEventTest {

        @Test
        @DisplayName("대기열 사용자 작업 공간 이동 기능을 트리거한다.")
        void moveUserToRunningRoom() {
            // given
            long performanceId = 1;

            String email = "email@email.com";
            waitingSystem.enterWaitingRoom(email, performanceId);

            // when
            long remainingCount = waitingSystem.pollRemainingCountAndTriggerEvents(email, performanceId);

            System.out.printf("남은 인원 수: %d\n", remainingCount);

            // then
            Set<String> members = rawRunningRoom.range("running_room::" + performanceId, 0, -1);
            System.out.println("회원 목록 출력 " + members);
            assertThat(waitingSystem.isInRunningRoom(email, performanceId)).isTrue();
        }
    }

    @Nested
    @DisplayName("마지막 폴링 이벤트 발행 시")
    class PublishLastPollingEventTest {

        @Test
        @DisplayName("작업 공간의 사용자 만료 시간을 업데이트한다.")
        void updateRunningMemberExpiredTime() {
            // given
            long performanceId = 1;
            String email = "email@email.com";

            waitingSystem.enterWaitingRoom(email, performanceId);

            // when
            waitingSystem.pollRemainingCountAndTriggerEvents(email, performanceId);

            // then
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    rawRunningRoom.rangeWithScores(getRunningRoomKey(performanceId), 0, -1);
            assertThat(tuples)
                    .hasSize(1)
                    .first()
                    .satisfies(
                            tuple -> {
                                ZonedDateTime memberExpiredTime =
                                        ZonedDateTime.ofInstant(
                                                Instant.ofEpochSecond(tuple.getScore().longValue()),
                                                ZoneId.of("Asia/Seoul"));
                                assertThat(memberExpiredTime)
                                        .isCloseTo(
                                                ZonedDateTime.now().plusMinutes(5),
                                                within(5, ChronoUnit.MINUTES));
                            });
        }

        @Test
        @DisplayName("이메일에 해당하는 사용자만 업데이트한다.")
        void updateOnlyInputMember() {
            // given
            long performanceId = 1;
            String email = "email@email.com";
            String anotherEmail = "anotherEmail@email.com";

            waitingSystem.enterWaitingRoom(email, performanceId);
            waitingSystem.enterWaitingRoom(anotherEmail, performanceId);
            waitingSystem.processExpiredAndMoveUsersToRunning(performanceId);

            // when
            waitingSystem.pollRemainingCountAndTriggerEvents(email, performanceId);

            // then
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    rawRunningRoom.rangeWithScores(getRunningRoomKey(performanceId), 0, -1);
            ZSetOperations.TypedTuple<String> emailMember =
                    tuples.stream()
                            .filter(tuple -> Objects.equals(tuple.getValue(), email))
                            .findFirst()
                            .get();
            ZSetOperations.TypedTuple<String> anotherEmailMember =
                    tuples.stream()
                            .filter(tuple -> Objects.equals(tuple.getValue(), anotherEmail))
                            .findFirst()
                            .get();
            assertThat(getTime(Objects.requireNonNull(emailMember.getScore())))
                    .isCloseTo(ZonedDateTime.now().plusMinutes(5), within(1, ChronoUnit.MINUTES));
            assertThat(getTime(Objects.requireNonNull(anotherEmailMember.getScore())))
                    .isCloseTo(ZonedDateTime.now().plusSeconds(30), within(5, ChronoUnit.SECONDS));
        }

        private ZonedDateTime getTime(Double score) {
            return ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(score.longValue()), ZoneId.of("Asia/Seoul"));
        }
    }
}