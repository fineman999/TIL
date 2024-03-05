package io.chan.springbatch.session09.adapter;

public class CustomService<T> {
    public void customWrite(T item) {
        System.out.println("Writing: " + item);
    }
}
