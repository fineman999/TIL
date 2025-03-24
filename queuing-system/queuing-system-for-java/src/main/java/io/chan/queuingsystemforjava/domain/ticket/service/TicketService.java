package io.chan.queuingsystemforjava.domain.ticket.service;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.TicketingException;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.repository.MemberRepository;
import io.chan.queuingsystemforjava.domain.ticket.dto.response.TicketElement;
import io.chan.queuingsystemforjava.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    protected final MemberRepository memberRepository;
    protected final TicketRepository ticketRepository;

    public ItemResult<TicketElement> selectMyTicket(String memberEmail) {
        Member member =
                memberRepository
                        .findByEmail(memberEmail)
                        .orElseThrow(() -> new TicketingException(ErrorCode.NOT_FOUND_MEMBER));

        List<TicketElement> tickets =
                ticketRepository.findAllByMember(member).stream().map(TicketElement::of).toList();

        return ItemResult.of(tickets);
    }
}