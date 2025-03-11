package io.chan.queuingsystemforjava.domain.member.repository;

import io.chan.queuingsystemforjava.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
