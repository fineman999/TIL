package io.chan.hellospring.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private Long id;
    private String name;

    public static User of(final Long id, final String name) {
        return new User(id, name);
    }
}
