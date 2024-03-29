package io.chan.bookservice.domain.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReturned extends ItemRented {
    public ItemReturned(final IDName idName, final Item item, final long point) {
        super(idName, item, point);
    }
}
