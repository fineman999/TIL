package io.chan.userservice.application.outputport;

import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.IDName;

/**
 * 회원번호로 회원로딩, IDName으로 회원로딩,회원저장 기능
 */
public interface MemberOutPutPort {
    Member save(Member member);
    Member loadMember(long memberNo);
    Member loadMemberByIdName(IDName idName);
}
