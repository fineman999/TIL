package org.hello.chapter01.item07;

import java.util.EmptyStackException;

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        // 다 쓴 참조 해제
        elements[size] = null;
        return result;
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보한다.
     * 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
     * 배열 크기를 늘리는 작업은 배열을 복사해야 하므로 비용이 크다.
     * 따라서 이 메서드에서는 가능한 한 크기를 늘리는 횟수를 줄이기 위해
     * 배열을 늘리기 전에 현재 배열이 꽉 찼는지 확인한다.
     */
    private void ensureCapacity() {
        if (elements.length == size) {
            elements = java.util.Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
