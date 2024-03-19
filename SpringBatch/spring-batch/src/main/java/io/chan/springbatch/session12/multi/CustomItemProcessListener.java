package io.chan.springbatch.session12.multi;

import io.chan.springbatch.session08.db.paging.Customer;
import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener<Customer, Customer> {
    @Override
    public void beforeProcess(Customer item) {
        ItemProcessListener.super.beforeProcess(item);
    }

    @Override
    public void afterProcess(Customer item, Customer result) {
        System.out.println("Process thread: " + Thread.currentThread().getName() + " - " + item);
    }

    @Override
    public void onProcessError(Customer item, Exception e) {
        ItemProcessListener.super.onProcessError(item, e);
    }
}
