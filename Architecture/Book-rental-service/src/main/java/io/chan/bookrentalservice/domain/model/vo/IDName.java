package io.chan.bookrentalservice.domain.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IDName {
    private final String id;
    private final String name;

    public static IDName createIDName(String id, String name) {
        return new IDName(id, name);
    }
}
