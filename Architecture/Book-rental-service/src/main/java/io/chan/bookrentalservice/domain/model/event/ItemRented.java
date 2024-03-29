package io.chan.bookrentalservice.domain.model.event;

import io.chan.bookrentalservice.domain.model.vo.IDName;
import io.chan.bookrentalservice.domain.model.vo.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 아이템 대여됨 이벤트
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRented {
    private final IDName idName;
    private final Item item;
    private final long point;

    public static ItemRented of(final IDName idName, final Item item, final long point) {
        return new ItemRented(idName, item, point);
    }

}
