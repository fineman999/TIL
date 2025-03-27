package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import java.util.Set;

/**
 * 순번 기반 대기열 관리
 * 용도: 순번(waitingCount)을 기준으로 정렬된 대기열을 관리하며, 순차적으로 사용자 꺼냄.
 * 특징: 스코어(순번)로 정렬된 집합 제공, 범위 기반 조회 및 삭제 가능.
 */
public interface WaitingLine {
    void enter(String email, long performanceId, long waitingCount);

    Set<String> pullOutMembers(long performanceId, long availableToRunning);
}
