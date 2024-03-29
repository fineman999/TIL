package io.chan.bestbookservice.domain.event;

import io.chan.bestbookservice.domain.vo.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 아이템 대여됨 이벤트
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRented {
    private IDName idName;
    private Item item;
    private long point;

    public static ItemRented of(final IDName idName, final Item item, final long point) {
        return new ItemRented(idName, item, point);
    }

}
