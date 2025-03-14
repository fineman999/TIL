package io.chan.queuingsystemforjava.domain.member.dto.response;

public record CreateMemberResponse(
        Long memberId
) {
    public static CreateMemberResponse of(Long memberId) {
        return new CreateMemberResponse(memberId);
    }
}
