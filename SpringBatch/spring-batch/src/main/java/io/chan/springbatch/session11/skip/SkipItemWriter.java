package io.chan.springbatch.session11.skip;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

import java.util.concurrent.atomic.AtomicInteger;

public class SkipItemWriter implements ItemWriter<String> {
    private AtomicInteger cnt = new AtomicInteger(0);

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        for (String item : chunk.getItems()) {
            if (item.equals("-12")) {
                throw new SkippableException("Writer failed. Attempt " + cnt.incrementAndGet());
            }
            System.out.println("ItemWriter: " + item);
        }
    }
}
