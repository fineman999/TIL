package org.hello.chapter05.item29.technique1;

import java.util.EmptyStackException;
import java.util.List;

// E[]를 이용한 제네릭 스택
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // 배열을 사용한 코드를 제네릭으로 만드는 방법 1
    // 배열 elements는 push(E)로 넘어온 E 인스턴스만 담는다.
    // 따라서 타입 안전성을 보장하지만,
    // 이 배열의 런타임 타입은 E[]가 아닌 Object[]다!
    @SuppressWarnings("unchecked")
    public Stack() {
        // elements = new E[DEFAULT_INITIAL_CAPACITY]; // 컴파일 에러
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY]; // 비검사 형변환 경고
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        E result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = java.util.Arrays.copyOf(elements, 2 * size + 1); // 비검사 형변환 경고
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
