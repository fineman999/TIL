package io.chan.userservice.domain.event;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
public class Item {
    private Long no;
    private String title;

    public static Item createItem(Long no, String title) {
        return new Item(no, title);
    }

}
