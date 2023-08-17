package org.hello.chapter05.item26.raw;

public class UseRawType<E> {
    private E e;

    public static void main(String[] args) {
//        System.out.println(UseRawType<Integer>.class); // 컴파일 에러

        UseRawType<Integer> useRawType = new UseRawType<>();

        System.out.println(useRawType instanceof UseRawType<Integer>); // 의미 없다.
    }
}
