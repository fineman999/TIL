package io.chan.userservice.application.usecase;

import io.chan.userservice.domain.vo.IDName;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;

/**
 * 회원 IDName으로 조회하여 로딩된 회원 에게 포인트 사용을 위임
 * 포인트 적립된 회원을 저장
 */
public interface UsePointUseCase {
    MemberOutPutDTO userPoint(IDName idName, long point);
}
