package io.chan.paymentservice.framework.web;

import io.chan.paymentservice.application.usecase.SaveMemberUseCase;
import io.chan.paymentservice.framework.web.dto.MemberInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final SaveMemberUseCase saveMemberUseCase;

    @PostMapping
    public void saveMember(
            @RequestBody MemberInputDTO memberInputDTO
    ) {
        saveMemberUseCase.saveMember(memberInputDTO);
    }
}
