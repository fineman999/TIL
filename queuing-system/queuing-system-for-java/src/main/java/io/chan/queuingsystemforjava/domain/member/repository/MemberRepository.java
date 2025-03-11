package io.chan.queuingsystemforjava.domain.member.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    public Member getById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("멤버를 찾을수 없습니다."));
    }
}
