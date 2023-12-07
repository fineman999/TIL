package io.chan.springsecurityresources.controller;

import lombok.Builder;
import org.springframework.security.core.Authentication;

@Builder
public record OpaqueDto (
        boolean active,
        Authentication authentication,
        Object principal

){
}
