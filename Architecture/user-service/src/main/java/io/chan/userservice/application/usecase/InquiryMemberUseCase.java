package io.chan.userservice.application.usecase;

import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;

/**
 * 회원번호로 조회하여 조회된 회원을 DTO로 변환하여 반환
 */
public interface InquiryMemberUseCase {
    MemberOutPutDTO inquiryMember(long memberId);
}
