package io.chan.springbatch.session07.item;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(Chunk<? extends Customer> chunk) throws Exception {
        for (Customer item : chunk.getItems()) {
            System.out.println(">> " + item);
        }
    }
}
