package io.chan.springsecurityoauth2v2;

import lombok.Builder;

@Builder
public record ClientDto(
        String accessToken,
        String refreshToken,
        String principalName,
        String clientName
) {


}
