package io.chan.springbatch.session11.skip;

import org.springframework.batch.item.ItemProcessor;

import java.util.concurrent.atomic.AtomicInteger;

public class SkipItemProcessor implements ItemProcessor<String, String> {
    private AtomicInteger cnt = new AtomicInteger(0);
    @Override
    public String process(String item) throws Exception {
        if (item.equals("6") || item.equals("7")){
            throw new SkippableException("Process failed. Attempt " + cnt.incrementAndGet());
        }
        System.out.println("Processing: " + item);
        return String.valueOf(Integer.parseInt(item) * -1);
    }
}
