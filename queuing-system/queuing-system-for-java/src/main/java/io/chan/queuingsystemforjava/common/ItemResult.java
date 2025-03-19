package io.chan.queuingsystemforjava.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResult<T> {

    private List<T> items;

    public static <T> ItemResult<T> of(List<T> items) {
        return new ItemResult<>(items);
    }
}
