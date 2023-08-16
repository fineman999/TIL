package org.hello.chapter04.item24.localclass;

public class MyClass {
    private int number = 10;

    void print() {
        class LocalClass {
            public void print() {
                System.out.println(number);
            }
        }

        LocalClass localClass = new LocalClass();
        localClass.print();
    }
}
