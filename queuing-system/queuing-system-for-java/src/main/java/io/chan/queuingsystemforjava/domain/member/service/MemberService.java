package io.chan.queuingsystemforjava.domain.member.service;

import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.domain.member.dto.request.CreateMemberRequest;
import io.chan.queuingsystemforjava.domain.member.dto.response.CreateMemberResponse;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateMemberResponse saveMember(CreateMemberRequest request) {
        memberRepository
                .findByEmail(request.email())
                .ifPresent(
                        member -> {
                            throw new TicketingException(ErrorCode.DUPLICATED_EMAIL);
                        });
        Member member = Member.create(request.email(), passwordEncoder.encode(request.password()), MemberRole.USER);

        memberRepository.save(member);
        return CreateMemberResponse.of(member.getMemberId());
    }
}
