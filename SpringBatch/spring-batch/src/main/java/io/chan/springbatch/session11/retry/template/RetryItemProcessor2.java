package io.chan.springbatch.session11.retry.template;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryItemProcessor2 implements ItemProcessor<String, Customer> {

    private final RetryTemplate retryTemplate;
    private final AtomicInteger cnt = new AtomicInteger(0);

    public RetryItemProcessor2(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Customer process(String item) throws Exception {
        Classifier<Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

        return retryTemplate.execute(new RetryCallback<Customer, RuntimeException>() {
            @Override
            public Customer doWithRetry(RetryContext context) throws RuntimeException {
                if (item.equals("2") || item.equals("3")) {
                    cnt.incrementAndGet();
                    throw new RuntimeException("Process failed. Attempt " + cnt.get());
                }
                return new Customer(item);
            }
        }, new RecoveryCallback<Customer>() {
            @Override
            public Customer recover(RetryContext context) throws Exception {
                return new Customer("Recovery");
            }
        }, new DefaultRetryState(item, rollbackClassifier));
    }
}
