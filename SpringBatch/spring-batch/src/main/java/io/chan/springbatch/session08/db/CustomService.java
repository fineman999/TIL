package io.chan.springbatch.session08.db;

public class CustomService<T> {
    private int cnt = 0;

    public T customRead() {
        return (T) ("Custom Read " + cnt++);
    }
}
