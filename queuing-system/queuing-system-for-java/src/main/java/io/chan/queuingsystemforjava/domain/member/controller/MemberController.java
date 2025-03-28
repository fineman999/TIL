package io.chan.queuingsystemforjava.domain.member.controller;

import io.chan.queuingsystemforjava.domain.member.dto.request.CreateMemberRequest;
import io.chan.queuingsystemforjava.domain.member.dto.response.CreateMemberResponse;
import io.chan.queuingsystemforjava.domain.member.dto.response.GetAllUsersResponse;
import io.chan.queuingsystemforjava.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember(
            @RequestBody @Valid CreateMemberRequest request) {
        CreateMemberResponse response = memberService.saveMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        return ResponseEntity.ok(memberService.getAllUsers());
    }
}
