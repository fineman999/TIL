package org.hello.chapter05.item29.object;

import java.util.EmptyStackException;
import java.util.List;

// Object를 이용한 제네릭 스택
public class Stack {
private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // 생성자
    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    // push 메서드
    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    // pop 메서드
    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    // 원소를 위한 공간을 적어도 하나 이상 확보한다.
    private void ensureCapacity() {
        if (elements.length == size) {
            Object[] oldElements = elements;
            elements = new Object[2 * size + 1];
            System.arraycopy(oldElements, 0, elements, 0, size);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        for (String arg: List.of("hello", "world", "java")) {
            stack.push(arg);
        }

        while (!stack.isEmpty()) {
            System.out.println((String) stack.pop());
        }
    }
}
