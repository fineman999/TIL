package io.chan.springbatch.session11.retry.api;

import io.chan.springbatch.session11.retry.RetryableException;
import org.springframework.batch.item.ItemProcessor;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryItemProcessor implements ItemProcessor<String, String> {
    private AtomicInteger cnt = new AtomicInteger(0);
    @Override
    public String process(String item) throws Exception {
        // 예외가 발생하면 chunk의 처음 단계부터 다시 시작한다.
        // 그럼 다시 1부터 시작하게 된다.
        // 그래서 2와 3은 무한 반복된다.
        // 그러므로 스킵을 사용하는 것이 좋다.
        if (item.equals("2") || item.equals("3")){
            cnt.incrementAndGet();
            throw new RetryableException("Process failed. Attempt " + cnt.get());
        }
        System.out.println("Processing: " + item);
        return item;
    }
}
