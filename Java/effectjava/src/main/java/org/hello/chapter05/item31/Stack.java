package org.hello.chapter05.item31;
import java.util.ArrayList;
import java.util.Collection;
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

    // 생성자(producer) 매개변수에 한정적 와일드카드 타입 적용
    public void pushAll(Iterable<? extends E> src) {
        for (E e: src) {
            push(e);
        }
    }

    // 소비자(consumer) 매개변수에 와일드카드 타입 적용
    public void popAll(Collection<? super E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = java.util.Arrays.copyOf(elements, 2 * size + 1); // 비검사 형변환 경고
        }
    }

    public static void main(String[] args) {
        Stack<Number> stack = new Stack<>();
        Iterable<Integer> integers = List.of(1, 2, 3, 4, 5);
        stack.pushAll(integers);
        Iterable<Double> doubles = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        stack.pushAll(doubles);

        Collection<Object> objects = new ArrayList<>();
        stack.popAll(objects);
    }
}
