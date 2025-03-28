package io.chan.queuingsystemforjava.domain.member.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    public Optional<Member> findById(final Long aLong) {
        return memberJpaRepository.findById(aLong);

    }

    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }
}
