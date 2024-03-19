package io.chan.userservice.application.inputport;

import io.chan.userservice.application.outputport.MemberOutPutPort;
import io.chan.userservice.application.usecase.SavePointUseCase;
import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.IDName;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class SavePointInputPort implements SavePointUseCase {
    private final MemberOutPutPort memberOutPutPort;
    @Override
    public MemberOutPutDTO savePoint(final IDName idName, final long point) {
        Member loadMember = memberOutPutPort.loadMemberByIdName(idName);
        loadMember.savePoint(point);
        return MemberOutPutDTO.from(loadMember);
    }
}
