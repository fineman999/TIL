package io.chan.queuingsystemforjava.domain.waitingsystem.waiting;

/**
 * 카운터 관리
 * 용도: 공연별 대기 순번을 생성하고 증가시킴.
 * 특징: 단일 값으로 카운터를 관리하며, 원자적 증가 연산 제공.
 */
public interface WaitingCounter {
    long getNextCount(long performanceId);
}
