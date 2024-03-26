package io.chan.bookrentalservice.domain.model.event;

import io.chan.bookrentalservice.domain.model.vo.IDName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OverdueCleared implements Serializable {

    @Serial
    private static final long serialVersionUID = 12343L;

    private final IDName idName;
    private final long point;

    public static OverdueCleared of(final IDName idName, final long point) {
        return new OverdueCleared(idName, point);
    }
}
