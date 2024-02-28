package io.chan.springbatch.session07.item;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

public class CustomItemReader implements ItemReader<Customer> {

    private final List<Customer> items;
    public CustomItemReader(List<Customer> items) {
        this.items = items;
    }
    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!items.isEmpty()) {
            return items.remove(0);
        }

        return null;
    }
}
