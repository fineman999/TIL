package io.chan.queuingsystemforjava.domain.member.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String email,
        String accessToken,
        String refreshToken
) {
}
