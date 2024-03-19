package io.chan.springbatch.session12.multi;

import io.chan.springbatch.session08.db.paging.Customer;
import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener<Customer> {
    @Override
    public void beforeRead() {
        ItemReadListener.super.beforeRead();
    }

    @Override
    public void afterRead(Customer item) {
        System.out.println("Read thread: " + Thread.currentThread().getName() + " - " + item);
    }

    @Override
    public void onReadError(Exception ex) {
        ItemReadListener.super.onReadError(ex);
    }
}
