package org.hello.chapter05.item29.technique2;

import java.util.EmptyStackException;
import java.util.List;

// Object[]를 이용한 제네릭 스택
public class Stack<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    // 비검사 경고를 적절히 숨긴다.
    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        // push에서 E 타입만 허용하므로 이 형변환은 안전하다.
        @SuppressWarnings("unchecked")
        E result = (E) elements[--size];

        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = java.util.Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        for (String arg: List.of("hello", "world", "java")) {
            stack.push(arg);
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
