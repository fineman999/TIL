package io.chan.userservice.domain.event;

import io.chan.userservice.domain.vo.IDName;
import lombok.Getter;


@Getter
public class ItemReturned extends ItemRented {
    public ItemReturned(final IDName idName, final Item item, final long point) {
        super(idName, item, point);
    }
}
