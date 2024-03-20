package io.chan.bestbookservice.domain.vo;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Item {
    private Long no;
    private String title;

    public static Item of(Long no, String title) {
        return new Item(no, title);
    }
}
