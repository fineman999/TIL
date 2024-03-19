package io.chan.userservice.application.usecase;

import io.chan.userservice.framwork.web.dto.MemberInfoDTO;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;

/**
 * 회원 객체에게 회원생성을 위임한 뒤 생성된 회원을 저장함.
 */
public interface AddMemberUseCase {
    MemberOutPutDTO addMember(MemberInfoDTO memberInfoDTO);

}
