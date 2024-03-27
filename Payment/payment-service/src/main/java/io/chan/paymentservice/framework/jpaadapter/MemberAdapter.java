package io.chan.paymentservice.framework.jpaadapter;

import io.chan.paymentservice.application.outputport.MemberOutputPort;
import io.chan.paymentservice.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberAdapter implements MemberOutputPort {
    private final MemberJpaRepository memberJpaRepository;
    @Override
    public void saveMember(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberJpaRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );
    }
}
