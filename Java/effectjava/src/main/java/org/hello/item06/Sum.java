package org.hello.item06;

public class Sum {
    public static long sumWithAutoBoxing() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        return sum;
    }

    public static long sum() {
        long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        return sum;
    }
}
