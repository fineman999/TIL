package io.chan.userservice.application.inputport;

import io.chan.userservice.application.outputport.MemberOutPutPort;
import io.chan.userservice.application.usecase.AddMemberUseCase;
import io.chan.userservice.domain.model.Member;
import io.chan.userservice.domain.vo.Email;
import io.chan.userservice.domain.vo.IDName;
import io.chan.userservice.domain.vo.Password;
import io.chan.userservice.framwork.web.dto.MemberInfoDTO;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AddMemberInputPort implements AddMemberUseCase {
    private final MemberOutPutPort memberOutPutPort;
    @Override
    public MemberOutPutDTO addMember(final MemberInfoDTO memberInfoDTO) {
        IDName idName = IDName.create(memberInfoDTO.id(), memberInfoDTO.name());
        Password pwd = Password.create(memberInfoDTO.passWord());
        Email email = Email.create(memberInfoDTO.email());
        Member addedMember = Member.register(idName, pwd, email);
        Member savedMember = memberOutPutPort.save(addedMember);
        return MemberOutPutDTO.from(savedMember);
    }
}
