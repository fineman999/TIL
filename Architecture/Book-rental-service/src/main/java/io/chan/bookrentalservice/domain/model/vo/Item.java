package io.chan.bookrentalservice.domain.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {
    private final Long no;
    private final String title;

    public static Item createItem(Long no, String title) {
        return new Item(no, title);
    }

}
