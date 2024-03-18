package io.chan.userservice.domain.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IDName {
    private String id;
    private String name;

    public static IDName create(String id, String name) {
        return new IDName(id, name);
    }
}
