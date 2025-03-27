package io.chan.queuingsystemforjava.domain.waitingsystem.service;

import io.chan.queuingsystemforjava.domain.waitingsystem.running.RedisRunningCounter;
import io.chan.queuingsystemforjava.domain.waitingsystem.running.RedisRunningRoom;
import io.chan.queuingsystemforjava.support.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class RedisRunningManagerTest extends BaseIntegrationTest {

    @Autowired
    private RedisRunningManager runningManager;

    @Autowired private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> rawRunningCounter;
    private ZSetOperations<String, String> rawRunningRoom;


    @TestConfiguration
    static class RedisRunningManagerTestConfiguration {
        @Autowired
        private RedisRunningCounter runningCounter;

        @Autowired
        private RedisRunningRoom runningRoom;

        @Bean
        public RedisRunningManager runningManager() {
            return new RedisRunningManager(runningRoom, runningCounter);
        }
    }

    @BeforeEach
    void setUp() {
        rawRunningCounter = redisTemplate.opsForValue();
        rawRunningRoom = redisTemplate.opsForZSet();
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    private String getRunningCounterKey(long performanceId) {
        return "running_counter::" + performanceId;
    }

    private String getRunningRoomKey(long performanceId) {
        return "running_room::" + performanceId;
    }

    @Nested
    @DisplayName("러닝 카운트 조회 시")
    class GetRunningCountTest {

        @Test
        @DisplayName("작업 가능 공간으로 진입한 인원 수를 반환한다.")
        void getRunningCount() {
            // given
            long performanceId = 1;
            rawRunningCounter.setIfAbsent(getRunningCounterKey(performanceId), "23");

            // when
            long runningCount = runningManager.getRunningCount(performanceId);

            // then
            assertThat(runningCount).isEqualTo(23);
        }

        @Test
        @DisplayName("카운트가 존재하지 않으면 0부터 시작한다.")
        void startCounterWithZeroValue() {
            // given
            long performanceId = 1;

            // when
            long runningCount = runningManager.getRunningCount(performanceId);

            // then
            assertThat(runningCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("작업 가능 인원 조회 시")
    class GetAvailableToRunning {

        @Test
        @DisplayName("수용 가능한 인원이 0보다 작으면 0을 반환한다.")
        void returnZero_WhenLessThanZero() {
            // given
            long performanceId = 1;
            Set<String> waitingMembers = new HashSet<>();
            for (int i = 0; i < 150; i++) {
                String email = "email" + i + "@email.com";
                waitingMembers.add(email);
            }
            runningManager.enterRunningRoom(performanceId, waitingMembers);

            // when
            long availableToRunning = runningManager.getAvailableToRunning(performanceId);

            // then
            assertThat(availableToRunning).isEqualTo(0);
        }

        @Test
        @DisplayName("수용 가능한 인원이 0보다 크면 수용 가능 인원을 반환한다.")
        void returnAvailable_WhenGreaterThanZero() {
            // given
            long performanceId = 1;
            Set<String> waitingMembers = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                String email = "email" + i + "@email.com";
                waitingMembers.add(email);
            }
            runningManager.enterRunningRoom(performanceId, waitingMembers);

            // when
            long runningCount = runningManager.getAvailableToRunning(performanceId);

            // then
            assertThat(runningCount).isEqualTo(80);
        }
    }

    @Nested
    @DisplayName("작업 가능 공간 입장 호출 시")
    class EnterRunningRoomTest {

        private Set<String> waitingMembers;
        private int waitingMemberCount;
        private long performanceId;

        @BeforeEach
        void setUp() {
            waitingMemberCount = 20;
            performanceId = 1;
            waitingMembers = new HashSet<>();
            for (int i = 0; i < waitingMemberCount; i++) {
                String email = "email" + i + "@email.com";
                waitingMembers.add(email);
            }
        }

        @Test
        @DisplayName("입장 인원만큼 작업 가능 공간 이동 인원 카운터를 증가시킨다.")
        void incrementRunningCounter() {
            // given

            // when
            runningManager.enterRunningRoom(performanceId, waitingMembers);

            // then
            long runningCount = runningManager.getRunningCount(performanceId);
            assertThat(runningCount).isEqualTo(waitingMemberCount);
        }

        @Test
        @DisplayName("작업 가능 공간에 사용자를 추가한다.")
        void enterRunningRoom() {
            // given

            // when
            runningManager.enterRunningRoom(performanceId, waitingMembers);

            // then
            Set<String> waitingMembers =
                    rawRunningRoom.range(getRunningRoomKey(performanceId), 0, -1);
            assertThat(waitingMembers).hasSize(waitingMemberCount);
        }
    }
}