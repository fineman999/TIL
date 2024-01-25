package org.example.exam09.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerAPIExample {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        int currentValue = atomicInteger.get();
        System.out.println("currentValue = " + currentValue);

        atomicInteger.set(20);
        System.out.println("atomicInteger.get() = " + atomicInteger.get());

        int oldValue = atomicInteger.getAndSet(30);
        System.out.println("oldValue = " + oldValue);

        int incrementAndGet = atomicInteger.incrementAndGet();
        System.out.println("incrementAndGet = " + incrementAndGet);

        IntUnaryOperator intUnaryOperator = operand -> operand * 10;

        int andUpdate = atomicInteger.getAndUpdate(intUnaryOperator);
        System.out.println("andUpdate = " + andUpdate);
        System.out.println("atomicInteger.get() = " + atomicInteger.get());

    }
}
