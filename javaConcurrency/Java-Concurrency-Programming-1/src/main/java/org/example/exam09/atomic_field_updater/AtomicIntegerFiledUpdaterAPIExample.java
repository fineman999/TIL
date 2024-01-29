package org.example.exam09.atomic_field_updater;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicIntegerFiledUpdaterAPIExample {
    private static AtomicIntegerFieldUpdater<MyClass> filedUpdater1;
    private static AtomicReferenceFieldUpdater<MyClass, String> filedUpdater2;

    public static void main(String[] args) {
        filedUpdater1 = AtomicIntegerFieldUpdater.newUpdater(MyClass.class, "value");
        filedUpdater2 = AtomicReferenceFieldUpdater.newUpdater(MyClass.class, String.class, "stringValue");

        MyClass myClass = new MyClass();
        filedUpdater1.addAndGet(myClass, 10); // 원자성 보장
        filedUpdater2.compareAndSet(myClass, null, "test"); // 원자성 보장

        System.out.println(myClass);
    }


    static class MyClass {
        private volatile int value;
        private volatile String stringValue;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "value=" + value +
                    ", stringValue='" + stringValue + '\'' +
                    '}';
        }
    }
}
