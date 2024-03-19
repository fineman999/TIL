package io.chan.springbatch.session12.multi;

import io.chan.springbatch.session11.retry.template.Customer;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class CustomItemWriteListener implements ItemWriteListener<Customer> {
    @Override
    public void beforeWrite(Chunk<? extends Customer> items) {
        ItemWriteListener.super.beforeWrite(items);
    }

    @Override
    public void afterWrite(Chunk<? extends Customer> items) {
        System.out.println("Write thread: " + Thread.currentThread().getName() + " - " + items);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends Customer> items) {
        ItemWriteListener.super.onWriteError(exception, items);
    }
}
