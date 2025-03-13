package io.chan.queuingsystemforjava.global.cache;

import lombok.Data;

@Data
public class UserToken {
    private String id;
    private String email;
    private String role;

    public UserToken(Long memberId, String email, String value) {
        this.id = memberId.toString();
        this.email = email;
        this.role = value;
    }
}
