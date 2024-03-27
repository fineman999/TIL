package io.chan.paymentservice.application.outputport;

import io.chan.paymentservice.domain.Member;

public interface MemberOutputPort {
    void saveMember(Member member);
}
