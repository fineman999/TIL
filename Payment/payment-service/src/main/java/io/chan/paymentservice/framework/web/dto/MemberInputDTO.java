package io.chan.paymentservice.framework.web.dto;

public record MemberInputDTO (
        String address,
        String email,
        String name
) {
}

