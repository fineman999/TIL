package io.chan.springbatch.session07.item;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {


    @Override
    public Customer process(Customer customer) throws Exception {
        customer.setName(customer.getName().toUpperCase());
        return customer;
    }
}
