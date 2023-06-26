package io.start.demo.security.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PreAuthorizeRequest {
    private final String email;

    @Builder
    public PreAuthorizeRequest(
            @JsonProperty("email") String email
         ) {
        this.email = email;
    }
}
