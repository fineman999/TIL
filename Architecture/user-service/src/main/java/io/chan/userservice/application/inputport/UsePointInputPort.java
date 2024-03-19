package io.chan.userservice.application.inputport;

import io.chan.userservice.application.outputport.MemberOutPutPort;
import io.chan.userservice.application.usecase.UsePointUseCase;
import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.IDName;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UsePointInputPort implements UsePointUseCase {
    private final MemberOutPutPort memberOutPutPort;
    @Override
    public MemberOutPutDTO userPoint(final IDName idName, final long point) {
        Member loadMember = memberOutPutPort.loadMemberByIdName(idName);
        loadMember.usePoint(point);
        return MemberOutPutDTO.from(loadMember);
    }
}
