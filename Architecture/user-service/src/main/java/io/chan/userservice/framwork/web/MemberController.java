package io.chan.userservice.framwork.web;

import io.chan.userservice.application.usecase.AddMemberUseCase;
import io.chan.userservice.application.usecase.InquiryMemberUseCase;
import io.chan.userservice.framwork.web.dto.MemberInfoDTO;
import io.chan.userservice.framwork.web.dto.MemberOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final AddMemberUseCase addMemberUsecase;
    private final InquiryMemberUseCase inquiryMemberUsecase;

    @PostMapping
    public ResponseEntity<MemberOutPutDTO> addMember(
            @RequestBody final MemberInfoDTO memberInfoDTO
            ) {
        return ResponseEntity.ok(addMemberUsecase.addMember(memberInfoDTO));
    }

    @GetMapping("/{memberNo}")
    public ResponseEntity<MemberOutPutDTO> getMember(
            @PathVariable final long memberNo
            ) {
        return ResponseEntity.ok(inquiryMemberUsecase.inquiryMember(memberNo));
    }

}
