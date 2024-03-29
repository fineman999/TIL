package io.chan.bookservice.domain.event;

import lombok.Getter;


@Getter
public class ItemReturned extends ItemRented {
    public ItemReturned(final IDName idName, final Item item, final long point) {
        super(idName, item, point);
    }
}
