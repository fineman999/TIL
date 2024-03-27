package io.chan.paymentservice.application.usecase;

import io.chan.paymentservice.framework.web.dto.MemberInputDTO;

public interface SaveMemberUseCase {
    void saveMember(MemberInputDTO memberInputDTO);
}
