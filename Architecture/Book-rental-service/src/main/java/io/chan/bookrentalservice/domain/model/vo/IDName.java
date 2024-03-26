package io.chan.bookrentalservice.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IDName implements Serializable {
    @Serial
    private static final long serialVersionUID = 12343L;
    private String id;
    private String name;

    public static IDName createIDName(String id, String name) {
        return new IDName(id, name);
    }
}
