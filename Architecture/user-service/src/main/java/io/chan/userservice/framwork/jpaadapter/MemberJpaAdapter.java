package io.chan.userservice.framwork.jpaadapter;

import io.chan.userservice.application.outputport.MemberOutPutPort;
import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.IDName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class MemberJpaAdapter implements MemberOutPutPort {
    private final MemberJpaRepository memberJpaRepository;
    @Override
    public Member save(final Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Member loadMember(final long memberNo) {
        return memberJpaRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Override
    public Member loadMemberByIdName(final IDName idName) {
        return memberJpaRepository.findMemberByIdName(idName)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}
