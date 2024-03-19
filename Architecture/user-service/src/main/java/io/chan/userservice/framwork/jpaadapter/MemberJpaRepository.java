package io.chan.userservice.framwork.jpaadapter;

import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.IDName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByIdName(IDName idName);
}
