package io.chan.paymentservice.application.inputport;

import io.chan.paymentservice.application.outputport.MemberOutputPort;
import io.chan.paymentservice.application.usecase.SaveMemberUseCase;
import io.chan.paymentservice.domain.Member;
import io.chan.paymentservice.framework.web.dto.MemberInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SaveMemberInputPort implements SaveMemberUseCase {
    private final MemberOutputPort memberOutputPort;
    @Override
    public void saveMember(MemberInputDTO memberInputDTO) {
        Member member = Member.of(
                memberInputDTO.address(),
                memberInputDTO.email(),
                memberInputDTO.name()
        );
        memberOutputPort.saveMember(member);
    }
}
