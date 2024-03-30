package io.chan.bookservice.domain.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventResult implements Serializable {
    private EventType eventType;
    private boolean success;
    private IDName idName;
    private Item item;
    private long point;

    public static EventResult of(final ItemRented itemRented, final EventType eventType, final boolean success) {
        return new EventResult(eventType, success, itemRented.getIdName(), itemRented.getItem(), itemRented.getPoint());
    }
}
