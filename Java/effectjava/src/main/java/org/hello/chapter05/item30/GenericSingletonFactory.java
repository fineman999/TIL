package org.hello.chapter05.item30;

import java.util.function.UnaryOperator;

// 제네릭 싱글턴 팩터리 패턴
public class GenericSingletonFactory {
    private static UnaryOperator<Object> IDENTITY_FUNCTION = (t) -> t;

    // IDENTITY_FUNCTION은 입력값을 그대로 반환하는 함수
    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FUNCTION;
    }
    public static void main(String[] args) {
        String[] strings = { "삼베", "대마", "나일론" };
        UnaryOperator<String> sameString = identityFunction();
        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }

        Number[] numbers = { 1, 2.0, 3L };
        UnaryOperator<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
    }
}
