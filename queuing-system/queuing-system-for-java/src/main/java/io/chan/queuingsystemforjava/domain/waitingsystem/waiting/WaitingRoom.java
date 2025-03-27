package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

import java.util.Set;

/**
 * 대기열 상태 관리
 * 용도: 각 공연(performanceId)별로 대기열에 있는 사용자(email)와 그 순번(waitingCount)을 저장.
 * 특징: 키-값 쌍으로 데이터를 관리하며, 중복 방지 및 빠른 조회 가능.
 */
public interface WaitingRoom {
    boolean enter(String email, long performanceId);

    void updateMemberInfo(String email, long performanceId, long waitingCount);

    void removeMemberInfo(String email, long performanceId);

    void removeMemberInfo(Set<String> emails, long performanceId);

    long getMemberWaitingCount(String email, long performanceId);
}