package io.chan.paymentservice.framework.jpaadapter;

import io.chan.paymentservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long>{
}
