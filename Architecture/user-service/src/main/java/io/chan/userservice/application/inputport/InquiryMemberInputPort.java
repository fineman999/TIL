package io.chan.userservice.application.inputport;

import io.chan.userservice.application.outputport.MemberOutPutPort;
import io.chan.userservice.application.usecase.InquiryMemberUseCase;
import io.chan.userservice.domain.model.Member;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class InquiryMemberInputPort implements InquiryMemberUseCase {
    private final MemberOutPutPort memberOutPutPort;
    @Override
    public MemberOutPutDTO inquiryMember(final long memberNo) {
        Member loadMember = memberOutPutPort.loadMember(memberNo);
        return MemberOutPutDTO.from(loadMember);
    }
}
