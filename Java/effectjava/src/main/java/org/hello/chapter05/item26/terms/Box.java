package org.hello.chapter05.item26.terms;

public class Box<E> {
    private E item;

    private void add(E item) {
        this.item = item;
    }

    private E get() {
        return item;
    }

    public static void main(String[] args) {
        Box<String> box = new Box<>();
        box.add("hello");
        printBox(box);
    }

    private static void printBox(Box<?> box) {
        System.out.println(box.get());
    }
}
