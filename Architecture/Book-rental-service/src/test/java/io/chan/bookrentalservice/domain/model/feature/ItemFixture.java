package io.chan.bookrentalservice.domain.model.feature;

import io.chan.bookrentalservice.domain.model.vo.Item;

public class ItemFixture {
    public static Item createItem() {
        final Long no = 1L;
        final String title = "노인과 바다";
        return Item.createItem(no, title);
    }
}
